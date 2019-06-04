package com.likuncheng.core.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.likuncheng.common.BaseApiService;
import com.likuncheng.common.BaseRedisService;
import com.likuncheng.common.ResponseBase;
import com.likuncheng.core.server.CoreServer;

/**
 * 抽奖核心控制类 这个类的很多方法都需要我们手动传入用户的账号 其实方法已经写好了的
 * 只是为了方便测试写的伪代码，真实的链条调用getAccountNumber 方法
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping(value = "/luckDraw")
public class CoreController extends BaseApiService {

	@Autowired
	private CoreServer coreServer;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	// 自定义线程池
	private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 8, 3, TimeUnit.SECONDS,
			new ArrayBlockingQueue<>(100));

	// 锁对象
	private static final String suo1 = "";

	// 锁对象
	private static final String suo2 = "";

	// 得到用户的账号信息 这是给整个链条测试用的 本地测试就不需要了 直接传入参数即可 方便一些
	@SuppressWarnings("unused")
	public String getAccountNumber(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String header = request.getHeader("loginToken");
			if (StringUtils.isEmpty(header)) {
				throw new Exception("用户未登录");
			}
			//判断redis中是否存在改logintoken
			String accountNumber = BaseRedisService.getString(header, stringRedisTemplate);
			if(StringUtils.isEmpty(accountNumber)) {
				throw new Exception("用户登录已过期，请从新登录");
			}
			return accountNumber;
		} catch (Exception e) {
			if(e != null) {
				throw e;
			}
			return null;
		}
	}

//	@RequestMapping(value = "/luckDraw")
//	public ResponseBase luckDraw(String type, HttpServletRequest request, HttpServletResponse response) {
//		synchronized (suo2) {
//			try {
//				Future<ResponseBase> submit = threadPoolExecutor.submit(new Callable<ResponseBase>() {
//
//					@Override
//					public ResponseBase call() throws Exception {
//						String accountNumber = getAccountNumber(request, response);
//						ResponseBase luckDraw = coreServer.luckDraw(accountNumber, Integer.parseInt(type));
//						return luckDraw;
//					}
//				});
//				return submit.get();
//			} catch (Exception e) {
//				System.out.println(e.getMessage());
//				return setResultError("抽奖出错了:"+e.getMessage());
//			}
//		}
//	}

	/**
	 * 抽奖实现代码
	 * 
	 * @param type          0：单次抽奖
	 * @param accountNumber 账号
	 * @param request
	 * @param response
	 * @return
	 */
//	@HystrixCommand(fallbackMethod="")  这是短路器 引号里面写 降级之后的方法
	@RequestMapping(value = "/luckDraw")
	public ResponseBase luckDraw(String type, String accountNumber, HttpServletRequest request,
			HttpServletResponse response) {
		synchronized (suo2) {
			try {
				// 开启线程进行执行抽奖操作
				Future<ResponseBase> submit = threadPoolExecutor.submit(new Callable<ResponseBase>() {
					@Override
					public ResponseBase call() throws Exception {
						// 抽奖操作
						ResponseBase luckDraw = coreServer.luckDraw(accountNumber, Integer.parseInt(type));
						return luckDraw;
					}
				});
				// 返回线程执行返回数据 也就是抽奖结果
				return submit.get();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return setResultError("抽奖出错了:" + e.getMessage());
			}
		}
	}

	/**
	 * 商品分解获得分解积分
	 * 
	 * @param commodityJson 得到需要分解的商品json信息
	 * @return
	 */
	@RequestMapping(value = "decompose")
	public ResponseBase decompose(String commodityJson) {
		try {
			// 由于这里直接传入的话 json里面有包含了服务器的特殊字符 不能直接传入 所以我们这里直接写一个 用于测试
			commodityJson = "[{\"accountNumber\":\"485796548745\",\"commodityNumber\":\"cNumber-9516972173778\",\"createTime\":\"2019-06-02 10:02:33.0\",\"lId\":3,\"version\":0},{\"accountNumber\":\"485796548745\",\"commodityNumber\":\"cNumber-3051889032680\",\"createTime\":\"2019-06-02 10:02:40.0\",\"lId\":4,\"version\":0},{\"accountNumber\":\"485796548745\",\"commodityNumber\":\"cNumber-3051889032680\",\"createTime\":\"2019-06-02 10:02:42.0\",\"lId\":5,\"version\":0},{\"accountNumber\":\"485796548745\",\"commodityNumber\":\"cNumber-3464936715941\",\"createTime\":\"2019-06-02 10:02:43.0\",\"lId\":6,\"version\":0},{\"accountNumber\":\"485796548745\",\"commodityNumber\":\"cNumber-3464936715941\",\"createTime\":\"2019-06-02 10:02:45.0\",\"lId\":7,\"version\":0}]";
			// 进行分解
			ResponseBase decompose = coreServer.decompose(commodityJson);
			return decompose;
		} catch (Exception e) {
			System.out.println("商品分解失败:" + e.getMessage());
			return setResultError("商品分解失败:" + e.getMessage());
		}
	}

	/**
	 * 查看抽奖到的东西 也就是暂存箱
	 * 
	 * @param accountNumber 账号
	 * @return
	 */
	@RequestMapping(value = "showDecompose")
	public ResponseBase showDecompose(String accountNumber) {
		try {
			// 得到所有的暂存箱中的商品
			ResponseBase showDecompose = coreServer.showDecompose(accountNumber);
			System.err.println("返回的商品的list:" + showDecompose.getData().toString());
			return showDecompose;
		} catch (Exception e) {
			System.out.println("用户查看暂存箱失败:" + e.getMessage());
			return setResultError("用户查看暂存箱失败:" + e.getMessage());
		}
	}

	/**
	 * 使用积分进行兑换商品
	 * 
	 * @param accountNumber 账号
	 * @param type          输入需要兑换的商品的对应的编号 0-8
	 * @return
	 */
	@RequestMapping(value = "exchange")
	public ResponseBase exchange(String accountNumber, String type) {
		try {
			// 执行兑换
			ResponseBase exchange = coreServer.exchange(accountNumber, type);
			return exchange;
		} catch (Exception e) {
			System.out.println("兑换异常:" + e.getMessage());
			return setResultError("兑换失败:" + e.getMessage());
		}
	}

	/**
	 * 充值钥匙
	 * 
	 * @param type 0：一个金钥匙 1：11个金钥匙其中包含送的一个
	 * @param accountNumber 账号
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "purchaseKey")
	public void purchaseKey(String type, String accountNumber, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		synchronized (suo1) {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=utf-8");
			PrintWriter writer = null;
			try {
				writer = response.getWriter();
				System.out.println("开始充值");
				//多线程进行执行
				Future<String> submit = threadPoolExecutor.submit(new Callable<String>() {
					@Override
					public String call() throws Exception {
						//调用增加钥匙方法
						ResponseBase purchaseKey = coreServer.purchaseKey(accountNumber, Integer.parseInt(type),
								request, response);
						if (purchaseKey.getRtnCode() == 500) {
							return purchaseKey.getMsg();
						}
						//返回增加的结果
						String string = purchaseKey.getData().toString();
						return string;
					}
				});
				String string = submit.get();
				//写入支付页面
				writer.println(string);
			} catch (Exception e) {
				System.out.println("发起购买钥匙失败:" + e.getMessage());
			} finally {
				writer.close();
				System.out.println("充值结束");
			}
		}
	}

}
