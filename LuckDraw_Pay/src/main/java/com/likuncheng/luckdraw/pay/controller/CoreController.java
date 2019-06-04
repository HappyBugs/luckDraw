package com.likuncheng.luckdraw.pay.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.internal.util.AlipaySignature;
import com.likuncheng.common.BaseApiService;
import com.likuncheng.common.ResponseBase;
import com.likuncheng.luckdraw.pay.server.CoreServer;

import alipay.config.AlipayConfig;

@RestController
@RequestMapping(value = "/aliPay")
public class CoreController extends BaseApiService {

	private static final String PAY_SUCCES = "pay_success";

	@Autowired
	private CoreServer coreServer;
	
	private static final String suo1 = "";
	
	private static final String suo2 = "";

	// 线程池
	public ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 8, 3, TimeUnit.SECONDS,
			new ArrayBlockingQueue<>(100));

	// 发起支付宝请求 需要支付token和订单id
	@RequestMapping(value = "payServer", method = RequestMethod.POST)
	public ResponseBase payServer(@RequestParam(value = "payToken") String payToken) {
		try {
			Future<ResponseBase> submit = threadPoolExecutor.submit(new Callable<ResponseBase>() {
				@Override
				public ResponseBase call() throws Exception {
					ResponseBase payServer = coreServer.PayServer(payToken);
					return payServer;
				}
			});
			return submit.get();
		} catch (Exception e) {
			System.out.println("发起支付请求出错了:"+e.getMessage());
			return setResultError("发起支付请求失败:"+e.getMessage());
		}
	}

//	// 发起支付宝请求 需要支付token和订单id
//	@RequestMapping(value = "payServer")
//	public ResponseBase payServer(@RequestParam(value = "payToken") String payToken, HttpServletRequest request,
//			HttpServletResponse response) throws IOException {
//		request.setCharacterEncoding("utf-8");
//		response.setContentType("text/html;charset=utf-8");
//		PrintWriter writer = response.getWriter();
//		try {
//			ResponseBase payServer = coreServer.PayServer(payToken);
//			if(payServer.getRtnCode() == 500) {
//				writer.println(payServer.getMsg());
//			}
//			String result = payServer.getData().toString();
//			writer.println(result);
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}finally {
//			writer.close();
//		}
//		return null;
//	}

	/**
	 * 异步地址
	 * 
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "payNotify")
	public String payNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
		synchronized (suo2) {
			try {
				Future<ResponseBase> submit = threadPoolExecutor.submit(new Callable<ResponseBase>() {
					@Override
					public ResponseBase call() throws Exception {
						Map<String, String> params = checkSign(request, response);
						ResponseBase payNotify = coreServer.payNotify(params);
						return payNotify;
					}
				});
				ResponseBase responseBase = submit.get();
				if (responseBase.getRtnCode() == 200) {
					return responseBase.getData().toString();
				}
				return responseBase.getData().toString();
			} catch (Exception e) {
				System.out.println("异步返回出错了:"+e.getMessage());
				return "fail";
			}
		}
	}

	/**
	 * 同步地址 只用于给用户展示效果
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws AlipayApiException
	 * @throws IOException
	 */
	@GetMapping(value = "payReturn")
	public void payReturn(HttpServletRequest request, HttpServletResponse response)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			Map<String, String> params = checkSign(request, response);
			// 如果验签失败返回false 如果成功才返回参数
			ResponseBase payReturn = coreServer.payReturn(params);
			// 像页面写如内容
			String payHtml = new String(payReturn.getData().toString());
			writer.println(payHtml);
		} catch (Exception e) {
			System.out.println("同步返回出错了:"+e.getMessage());
		} finally {
			writer.close();

		}
	}

//	/**
//	 * 同步地址 只用于给用户展示效果
//	 * 
//	 * @return
//	 * @throws UnsupportedEncodingException 
//	 * @throws AlipayApiException
//	 * @throws IOException
//	 */
//	@SuppressWarnings("null")
//	@GetMapping(value = "payReturn")
//	public ResponseBase payReturn(HttpServletRequest request, HttpServletResponse response) {
//		try {
//			request.setCharacterEncoding("utf-8");
//			Map<String, String> params = checkSign(request, response);
//			if(params != null || params.size() <=0) {
//				throw new Exception("支付宝同步返回结果失败");
//			}
//			return setResultSuccess("支付宝同步返回成功");
//		} catch (Exception e) {
//			return setResultError("支付宝同步返回异常:"+e.getMessage());
//		}
//	}

	/**
	 * 接收支付返回结果并验签 一起只能允许一个请求进去
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public synchronized Map<String, String> checkSign(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> params = new HashMap<String, String>();
		synchronized (suo1) {
			try {
				Map<String, String[]> requestParams = request.getParameterMap();
				for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
					String name = (String) iter.next();
					String[] values = (String[]) requestParams.get(name);
					String valueStr = "";
					for (int i = 0; i < values.length; i++) {
						valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
					}
					params.put(name, valueStr);
				}
				boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key,
						AlipayConfig.charset, AlipayConfig.sign_type);
				if (!signVerified) {
					throw new Exception("验签失败");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return params;
		}
	}

	/**
	 * 用于最后的同步回调显示地址 目的：通过from表单隐藏参数
	 * 
	 * @param request
	 * @param outTradeNo
	 * @param tradeNo
	 * @param totalAmount
	 * @return
	 */
	@PostMapping(value = "paySuccessReturn")
	public String paySuccessReturn(HttpServletRequest request, String outTradeNo, String tradeNo, String totalAmount) {
		request.setAttribute("outTradeNo", outTradeNo);
		request.setAttribute("tradeNo", tradeNo);
		request.setAttribute("totalAmount", totalAmount);
		return PAY_SUCCES;
	}

}
