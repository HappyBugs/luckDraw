package com.likuncheng.luckdraw.pay.serverImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import com.likuncheng.common.BaseApiService;
import com.likuncheng.common.BaseRedisService;
import com.likuncheng.common.ResponseBase;
import com.likuncheng.luckdraw.pay.entity.PayBean;
import com.likuncheng.luckdraw.pay.feign.GoldKeyFeign;
import com.likuncheng.luckdraw.pay.feign.OrderFeign;
import com.likuncheng.luckdraw.pay.mapper.CoreMapper;
import com.likuncheng.luckdraw.pay.server.CoreServer;
import alipay.config.AlipayConfig;

@Service
public class CoreServerImpl extends BaseApiService implements CoreServer {

	// mapper层
	@Autowired
	private CoreMapper coreMapper;

	// 引入order远程调用服务
	@Autowired
	private OrderFeign orderFeign;

	// redis
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private GoldKeyFeign goldKeyFeign;

	// 产生支付记录
	@Transactional
	@Override
	public ResponseBase createPayLog(PayBean payBean) {
		Integer createPayLog = 0;
		// 创建支付信息记录
		createPayLog = coreMapper.createPayLog(payBean);
		if (createPayLog <= 0) {
			return setResultError("创建支付订单失败");
		}
		return setResultSuccessData(createPayLog, "创建支付信息成功");
	}

	// 同步
	@Override
	public ResponseBase payReturn(Map<String, String> params) {
		// 封装一个from表单 用于隐藏支付宝同步回调的时候的参数
		String htmlFrom = "<form name='punchout_form' method='post' action='http://127.0.0.1/aliPay/paySuccessReturn'>"
				+ "<input type='hidden' name='outTradeNo' value='" + params.get("out_trade_no") + "'/>"
				+ "<input type='hidden' name='tradeNo' value='" + params.get("trade_no") + "'/>"
				+ "<input type='hidden' name='totalAmount' value='" + params.get("total_amount") + "'/>"
				+ "<input type='submit' value='立即支付' style='display:none'/>"
				+ "</form><script>document.forms[0].submit();</script>";
		return setResultSuccessData(htmlFrom, "成功封装为form表单");
	}

	// 来自order服务
	// 分布式事务 没有分布式事务就创建分布式事务
	@TxcTransaction(propagation = DTXPropagation.REQUIRED)
	@Transactional
	@Override
	public ResponseBase payNotify(Map<String, String> params) {
		synchronized (coreMapper) {
			try {
				// 订单号
				String outTradeNo = params.get("out_trade_no");
				// 支付宝交易号
				String tradeNo = params.get("trade_no");
				// 交易金额
				String totalAmount = params.get("total_amount");

				// 调用订单方法
				ResponseBase orderByOrderNumber = orderFeign.getOrderByOrderNumber(outTradeNo);
				if (orderByOrderNumber.getRtnCode() == 500) {
					throw new Exception("订单信息查找失败");
				}
				// 得到orderjson对象
				String orderJson = orderByOrderNumber.getData().toString();
				// 转换
				JSONObject parseObject = JSON.parseObject(orderJson);
				// 得到金额
				String newTotalAmount = (String) parseObject.get("money");
				// 如果金额不一致
				if (!newTotalAmount.equals(totalAmount)) {
					// 修改状态为 1正常金额 2异常金额
					parseObject.put("payState", 2);
					parseObject.put("updateTime", new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date()));
					String jsonString = parseObject.toJSONString();
					ResponseBase updatePsyState = this.orderFeign.updatePsyState(jsonString);
					// 200修改成功
					if (updatePsyState.getRtnCode() == 500) {
						throw new Exception("修改支付状态失败");
					}
					return setResultSuccessData("success", "异常金额");
				}
				// 修改状态为 1正常金额 2异常金额
				parseObject.put("payState", 1);
				parseObject.put("updateTime", new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date()));
				String jsonString = parseObject.toJSONString();
				//修改支付状态
				ResponseBase updatePsyState = this.orderFeign.updatePsyState(jsonString);
				// 200修改成功
				if (updatePsyState.getRtnCode() == 500) {
					throw new Exception("修改支付状态失败");
				}
				// 调用本地服务 修改支付记录
				PayBean payBeanByOutTradeNo = coreMapper.getPayBeanByOutTradeNo(outTradeNo);
				if (payBeanByOutTradeNo == null) {
					throw new Exception("查找支付信息失败");
				}
				//更新相关的属性进行 更新支付记录
				payBeanByOutTradeNo.setTradeNo(tradeNo);
				payBeanByOutTradeNo.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date()));
				Integer updatePayLog = coreMapper.updatePayLog(payBeanByOutTradeNo);
				if (updatePayLog <= 0) {
					throw new Exception("修改支付记录失败");
				}
				// 对钥匙的操作
				Integer type = (Integer) parseObject.get("type");
				Integer keyNumber = type == 0 ? 1 : 11;
				String accountNumber = parseObject.getString("accountNumber");
				//根据账号信息得到钥匙对象
				ResponseBase goldKeyByAccountNumber = goldKeyFeign.getGoldKeyByAccountNumber(accountNumber);
				//如果没有 那就是第一次充值
				if (goldKeyByAccountNumber.getRtnCode() == 500) {
					//添加钥匙对象
					ResponseBase createGoldKey = goldKeyFeign.createGoldKey(accountNumber, keyNumber.toString());
					if (createGoldKey.getRtnCode() == 200) {
					}
					throw new Exception("创建钥匙失败");
				}
				String goldKeyJson = goldKeyByAccountNumber.getData().toString();
				JSONObject goldKeyJSON = JSON.parseObject(goldKeyJson);
				Integer newKeyNumber = goldKeyJSON.getInteger("keyNumber");
				goldKeyJSON.put("keyNumber", (keyNumber + newKeyNumber));
				String newGoldKeyJson = goldKeyJSON.toJSONString();
				ResponseBase updateGoldKey = goldKeyFeign.updateGoldKey(newGoldKeyJson);
				if (updateGoldKey.getRtnCode() == 200) {
					throw new Exception("增加钥匙失败");
				}
			} catch (Exception e) {
				// 手动回滚
				System.out.println(e.getMessage());
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return setResultError("fail");
			}
			return setResultSuccessData("success", "支付成功");
		}

	}

	// 得到支付请求参数
	@SuppressWarnings("static-access")
	public PayBean getBayBean(String order) {
		PayBean payBean = null;
		try {
			// 解析json 赋值给payBean
			JSONObject jsonObject = new JSONObject();
			JSONObject parseObject = jsonObject.parseObject(order);
			String oId = parseObject.getString("orderNumber");
			String oUnitPrioe = parseObject.getString("money");
			String oDesoribe = parseObject.getString("orderDescribe");
			payBean = new PayBean(oId, oUnitPrioe, "购买金钥匙", oDesoribe);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return payBean;
	}

	// 支付宝发起支付请求
	public String alipayService(PayBean payBean) throws Exception {
		String result = "";
		try {
			// 获得初始化的AlipayClient
			AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
					AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key,
					AlipayConfig.sign_type);

			// 设置请求参数
			AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
			alipayRequest.setReturnUrl(AlipayConfig.return_url);
			alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

			// 商户订单号，商户网站订单系统中唯一订单号，必填
			String out_trade_no = payBean.getOutTradeNo();
			// 付款金额，必填
			String total_amount = payBean.getTotalAmount();
			// 订单名称，必填
			String subject = payBean.getSubject();
			// 商品描述，可空
			String body = payBean.getBody();
			// 封装支付请求内容参数
			alipayRequest.setBizContent("{\"out_trade_no\":\"" + out_trade_no + "\"," + "\"total_amount\":\""
					+ total_amount + "\"," + "\"subject\":\"" + subject + "\"," + "\"body\":\"" + body + "\","
					+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
			// 请求
			result = alipayClient.pageExecute(alipayRequest).getBody();
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	// 发起请求 传入支付token和orderid
	@Override
	public ResponseBase PayServer(String payToken) {
		String result = "";
		try {
			// 非空验证
			if (StringUtils.isEmpty(payToken)) {
				throw new Exception("参数不能为空");
			}
			// 判断reids中时候存在该缓存 也就是支付token是否过期
			Boolean hasKey = stringRedisTemplate.hasKey("test");
			System.out.println(hasKey);
			// orderNumber
			String orderNumber = BaseRedisService.getString(payToken.toString(), stringRedisTemplate);
			if (StringUtils.isEmpty(orderNumber)) {
				throw new Exception("支付token无效或已过期");
			}
			// 查询不需要事务 得到order订单信息
			ResponseBase orderByOrderNumber = orderFeign.getOrderByOrderNumber(orderNumber);
			if (orderByOrderNumber.getRtnCode() == 500) {
				throw new Exception("订单信息查找失败");
			}
			String orderJson = (String) orderByOrderNumber.getData();
			// 封装paybean对象
			PayBean payBean = getBayBean(orderJson);
			// 调用方法 发起支付宝支付请求
			result = alipayService(payBean);
			if (StringUtils.isEmpty(result)) {
				throw new Exception("支付宝返回结果异常");
			}
			// 保存支付宝日志
			Integer createPayLog = coreMapper.createPayLog(payBean);
			if (createPayLog <= 0) {
				throw new Exception("创建支付宝订单错误");
			}
		} catch (Exception e) {
			// 手动回滚事务
			return setResultError("发起支付请求失败:" + e.getMessage());
		}
		return setResultSuccessData(result, "发起支付信息成功");
	}

	// 更新日志
	@Override
	@Transactional
	public ResponseBase updatePayLog(PayBean payBean) {
		Integer updatePayLog = coreMapper.updatePayLog(payBean);
		if (updatePayLog <= 0) {
			return setResultError("修改支付记录失败");
		}
		return setResultError("修改支付记录成功");
	}

	@Override
	public ResponseBase getPayBeanByOutTradeNo(String outTradeNo) {
		PayBean payBeanByOutTradeNo = coreMapper.getPayBeanByOutTradeNo(outTradeNo);
		if (payBeanByOutTradeNo == null) {
			return setResultError("查找支付记录失败");
		}
		return setResultSuccessData(payBeanByOutTradeNo, "查找支付记录成功");
	}

}
