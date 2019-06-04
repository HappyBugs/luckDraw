package com.likuncheng.luckdraw.pay.server;

import java.util.Map;

import com.likuncheng.common.ResponseBase;
import com.likuncheng.luckdraw.pay.entity.PayBean;

public interface CoreServer {

	// 创建支付记录
	public ResponseBase createPayLog(PayBean payBean);

	// 同步返回地址
	public ResponseBase payReturn(Map<String, String> params);

	// 异步返回地址
	public ResponseBase payNotify(Map<String, String> params);

	// 发起支付请求
	public ResponseBase PayServer(String payToken);

	// 修改支付包记录
	public ResponseBase updatePayLog(PayBean payBean);

	// 创建支付记录
	public ResponseBase getPayBeanByOutTradeNo(String outTradeNo);

}
