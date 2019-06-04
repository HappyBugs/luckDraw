package com.likuncheng.luckdraw.pay.thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.likuncheng.common.ResponseBase;
import com.likuncheng.luckdraw.pay.feign.OrderFeign;

public class UpdatePsyState implements Callable<String> {

	private OrderFeign orderFeign;
	
	private JSONObject parseObject;
	
	private Integer state;

	public UpdatePsyState(OrderFeign orderFeign,JSONObject parseObject,Integer state) {
		this.orderFeign = orderFeign;
		this.parseObject = parseObject;
		this.state = state;
	}

	@Override
	public String call() throws Exception {
		updatePsyState();
		return "success";
	}

	// 修改订单状态
	@Transactional
	public void updatePsyState() throws Exception {
		try {
			// 修改状态为 1正常金额 2异常金额
			parseObject.replace("payState", state);
			parseObject.replace("updateTime", new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date()));
			String jsonString = parseObject.toJSONString();
			ResponseBase updatePsyState = this.orderFeign.updatePsyState(jsonString);
			// 200修改成功
			if (updatePsyState.getRtnCode() == 500) {
				throw new Exception("修改支付状态失败");
			}
		} catch (Exception e) {
			throw e;
		}
	}

}
