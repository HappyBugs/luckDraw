package com.likuncheng.core.serverImpl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicStampedReference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import com.likuncheng.Run2;
import com.likuncheng.common.BaseApiService;
import com.likuncheng.common.BaseRedisService;
import com.likuncheng.common.CreatePayToken;
import com.likuncheng.common.ResponseBase;
import com.likuncheng.core.entity.Commodity;
import com.likuncheng.core.entity.GetCommoditNumberUtils;
import com.likuncheng.core.entity.GoldKey;
import com.likuncheng.core.entity.LuckDrawLog;
import com.likuncheng.core.feigns.OrderFeign;
import com.likuncheng.core.feigns.PayFeign;
import com.likuncheng.core.mapper.CommodityMapper;
import com.likuncheng.core.mapper.GoldKeyMapper;
import com.likuncheng.core.mapper.LuckDrawLogMapper;
import com.likuncheng.core.server.CoreServer;

@Service
public class CoreServerImpl extends BaseApiService implements CoreServer {

	// 钥匙
	@Autowired
	private GoldKeyMapper goldKeyMapper;

	@Autowired
	private LuckDrawLogMapper luckDrawLogMapper;

	@Autowired
	private OrderFeign orderFeign;

	@Autowired
	private PayFeign payFeign;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private CommodityMapper commodityMapper;

	// 返回抽奖商品信息 进行封装
	//accountNumber：账号  type：0，消耗1个金钥匙进行一次抽奖  1：消耗10个金钥匙进行10次抽奖
	@Override
	@Transactional
	public ResponseBase luckDraw(String accountNumber, Integer type) {
		try {
			// 根据账号信息得到钥匙对象
			GoldKey goldKeyByAccountNumber = goldKeyMapper.getGoldKeyByAccountNumber(accountNumber);
			// 单抽
			int frequency = 1;
			if (type == 0) {
				if (goldKeyByAccountNumber.getKeyNumber() <= 0) {
					throw new Exception("抽奖失败,钥匙不足");
				}
				// 钥匙数量减一
				goldKeyByAccountNumber.setKeyNumber((goldKeyByAccountNumber.getKeyNumber() - 1));
				// 次数加一
				goldKeyByAccountNumber.setLuckNumber((goldKeyByAccountNumber.getLuckNumber() + 1));
				//标识抽奖次数
				frequency = 1;
			}
			if (type == 1) {
				if (goldKeyByAccountNumber.getKeyNumber() < 5) {
					throw new Exception("抽奖失败,钥匙不足");
				}
				// 钥匙数量减一
				goldKeyByAccountNumber.setKeyNumber((goldKeyByAccountNumber.getKeyNumber() - 5));
				// 次数加一
				goldKeyByAccountNumber.setLuckNumber((goldKeyByAccountNumber.getLuckNumber() + 5));
				frequency = 5;
			}
			// 更新结果
			Integer updateGoldKey = goldKeyMapper.updateGoldKey(goldKeyByAccountNumber);
			if (updateGoldKey <= 0) {
				throw new Exception("钥匙操作失败");
			}
			//保存抽奖到的商品信息
			Commodity commodityNumber = null;
			//保存抽奖到的商品名称
			List<String> commodityNames = new ArrayList<>();
			//保存所有的商品信息
			List<Commodity> commoditys = new ArrayList<>();
			//遍历商品 
			for (int i = 0; i < frequency; i++) {
				//得到所有的商品 
				commodityNumber = GetCommoditNumberUtils.getCommodityNumber();
				//添加商品
				commoditys.add(commodityNumber);
				//添加抽奖到的商品名称
				commodityNames.add(commodityNumber.getCommodityName());
			}
			//保存抽奖记录
			List<LuckDrawLog> luckDrawLogs = new ArrayList<>();
			//遍历获得的所有抽奖到的商品
			for (Commodity commodity : commoditys) {
				//实例化抽奖记录对象
				LuckDrawLog luckDrawLog = new LuckDrawLog(
						new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date()), accountNumber,
						commodity.getCommodityNumber());
				//保存抽奖记录
				Integer createLog = luckDrawLogMapper.createLog(luckDrawLog);
				if (createLog <= 0) {
					throw new Exception("保存抽奖记录失败");
				}
				//添加到集合
				luckDrawLogs.add(luckDrawLog);
			}
			// 返回结果
			return setResultSuccessData(commodityNames.toString(), "抽奖成功");
		} catch (Exception e) {
			// 手动回滚
			System.out.println("用户抽奖异常:" + e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return setResultError("抽奖失败:" + e.getMessage());
		}
	}

	// 一个全新的事务 不会受到影响  用于判断是否有未支付的订单 和创建订单以及payToken
	//accountNumber：账号  type：0，充值一个钥匙  1：充值10个钥匙，送一个钥匙
	@SuppressWarnings("unused")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@TxcTransaction(propagation = DTXPropagation.REQUIRED)
	public List<String> createOrder(String accountNumber, Integer type) throws Exception {
		//保存payToken和orderNumber
		List<String> result = new ArrayList<>();
		try {
			//查询该账号未支付的订单 内部封装start
			ResponseBase order = orderFeign.getOrderByAccountNumberAndPayState(accountNumber);
			// 存在该用户的订单
			if (order.getRtnCode() == 200) {
				//得到返回值
				String data = (String) order.getData();
				JSONObject parseObject = JSON.parseObject(data);
				//获得订单号
				String orderNumber = parseObject.getString("orderNumber");
				//得到支付token
				String payToken = parseObject.getString("payToken");
				//更新redis缓存
				BaseRedisService.setString(payToken, orderNumber, stringRedisTemplate, 1 * 60);
				//提交redis
				stringRedisTemplate.exec();
				//添加集合信息
				result.add(payToken);
				result.add(orderNumber);
				//返回数据
				return result;
			}
			// 如果不存在该订单  生成订单
			String payToken = CreatePayToken.createPayToekn();
			//调用方法创建订单
			ResponseBase createOrder = orderFeign.createOrder(accountNumber, type.toString(), payToken);
			//是否成功
			Integer rtnCode = createOrder.getRtnCode();
			if (rtnCode == 500) {
				throw new Exception("创建支付订单失败");
			}
			//得到订单号
			String orderNumber = (String) createOrder.getData();
			//保存redis
			BaseRedisService.setString(payToken, orderNumber, stringRedisTemplate, 1 * 60);
			//提交
			stringRedisTemplate.exec();
			//添加信息
			result.add(payToken);
			result.add(orderNumber);
			return result;
		} catch (Exception e) {
			System.out.println("创建订单和支付token异常:" + e.getMessage());
			stringRedisTemplate.discard();
			if (e != null) {
				throw e;
			}
			return null;
		}
	}

	// 购买钥匙
	//accountNumber：账号  type：0，充值一个钥匙  1：充值10个钥匙，送一个钥匙
	@SuppressWarnings("all")
	@Override
	public ResponseBase purchaseKey(String accountNumber, Integer type, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ResponseBase result = null;
		try {
			//得到payToken和orderNumber
			List<String> createOrder = createOrder(accountNumber, type);
			// 参数是token
			result = payFeign.payServer(createOrder.get(0));
			if (request == null) {
				throw new Exception("调用支付服务失败");
			}
			System.out.println("获取到的支付宝返回的请求结果是：" + result.getData());
			//返回调用结果 用于发起支付宝支付请求
			return setResultSuccessData(result.getData(), "发起支付请求成功");
		} catch (Exception e) {
			System.out.println("充值失败,系统异常:" + e.getMessage());
			stringRedisTemplate.discard();
			if (e != null) {
				throw e;
			}
			return setResultError("充值失败,系统异常:" + e.getMessage());
		}
	}

	// 商品分解 commodityJson 传递过来的需要分解的商品的json类型的信息
	@Override
	@Transactional
	public ResponseBase decompose(String commodityJson) {
		try {
			// 创建原子类 初始化值为0版本为0
			AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(0, 0);
			//根据传递的json进行解析，获得所有的抽奖记录中的商品
			List<LuckDrawLog> parseArray = JSON.parseArray(commodityJson, LuckDrawLog.class);
			//账号 
			String accountNumber = "";
			//遍历信息
			for (LuckDrawLog luckDrawLog : parseArray) {
				//调用分级商品的方法获得分解积分
				String exchangeByCommodityNumber = commodityMapper
						.getExchangeByCommodityNumber(luckDrawLog.getCommodityNumber());
				if (StringUtils.isEmpty(exchangeByCommodityNumber)) {
					throw new Exception("异常的商品信息");
				}
				// 得到当前对象的值
				Integer reference = atomicStampedReference.getReference();
				// 赋值动作 进行原子类操作
				atomicStampedReference.compareAndSet(reference, reference + Integer.parseInt(exchangeByCommodityNumber),
						atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
				//删除暂存箱中的商品
				Integer deleteLuckdrawLog = luckDrawLogMapper.deleteLuckdrawLog(luckDrawLog.getLId().toString());
				if (deleteLuckdrawLog <= 0) {
					throw new Exception("消除抽奖记录异常");
				}
				//得到账号信息
				accountNumber = luckDrawLog.getAccountNumber();
			}
			// 得到总积分
			Integer integral = atomicStampedReference.getReference();
			//得到该账号的钥匙对象
			GoldKey goldKeyByAccountNumber = goldKeyMapper.getGoldKeyByAccountNumber(accountNumber);
			goldKeyByAccountNumber.setIntegral(integral);
			//更新积分信息
			Integer addIntegral = goldKeyMapper.addIntegral(goldKeyByAccountNumber);
			if (addIntegral <= 0) {
				throw new Exception("用户增加积分失败");
			}
			// 返回获得的积分数量
			return setResultSuccessData(integral, "分解商品成功");
		} catch (Exception e) {
			System.out.println("用户分解商品异常:" + e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return setResultError("分解商品失败");
		}
	}

	//查看所有抽奖到的商品 也就是查看暂存箱
	@Override
	public ResponseBase showDecompose(String accountNumber) {
		try {
			//根据用户账号得到所有的暂存的商品
			List<LuckDrawLog> commoditys = luckDrawLogMapper.getCommoditys(accountNumber);
			if (commoditys == null || commoditys.size() <= 0) {
				throw new Exception("用户获取抽奖记录商品异常");
			}
			//转换格式
			String jsonString = JSON.toJSONString(commoditys);
			//返回数据
			return setResultSuccessData(jsonString, "用户获取抽奖记录商品成功");
		} catch (Exception e) {
			System.out.println("用户查看抽奖记录商品失败:" + e.getMessage());
			return setResultError("用户查看抽奖记录商品失败:" + e.getMessage());
		}
	}

	//消耗积分兑换商品  accountNumber：账号  type：需要兑换的商品的编号 0-8
	@Override
	@Transactional
	public ResponseBase exchange(String accountNumber, String type) {
		try {
			//类型转换并判断是否合法
			Integer number = Integer.parseInt(type);
			if (number < 0 || number > 8) {
				throw new Exception("编号错误，没有改商品的编号");
			}
			//得到所有的参与抽奖的商品
			List<Commodity> sommoditys = Run2.sommoditys;
			//得到用户需要兑换的商品
			Commodity commodity = sommoditys.get(number);
			//根据账号获得钥匙对象
			GoldKey goldKeyByAccountNumber = goldKeyMapper.getGoldKeyByAccountNumber(accountNumber);
			if (goldKeyByAccountNumber == null) {
				throw new Exception("没有改账号信息");
			}
			//剩余积分判断
			if (goldKeyByAccountNumber.getIntegral() < commodity.getExchange()) {
				throw new Exception("兑换失败，您的积分不足");
			}
			//对积分的减少操作
			goldKeyByAccountNumber.setIntegral(goldKeyByAccountNumber.getIntegral() - commodity.getExchange());
			Integer reduceIntegral = goldKeyMapper.reduceIntegral(goldKeyByAccountNumber);
			if (reduceIntegral <= 0) {
				throw new Exception("兑换异常，积分扣除失败");
			}
			//实例化记录对象
			LuckDrawLog luckDrawLog = new LuckDrawLog(new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date()),
					accountNumber, commodity.getCommodityNumber());
			//把兑换的商品放入暂存箱里面
			Integer createLog = luckDrawLogMapper.createLog(luckDrawLog);
			if(createLog<=0) {
				throw new Exception("保存兑换记录失败");
			}
			//类型转换，并返回数据
			String jsonString = JSON.toJSONString(commodity);
			return setResultSuccessData(jsonString, "兑换成功");
		} catch (Exception e) {
			System.out.println("兑换商品异常:" + e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return setResultError("兑换失败:" + e.getMessage());
		}
	}

}
