package com.likuncheng.luckdraw.pay.thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

import org.springframework.transaction.annotation.Transactional;

import com.likuncheng.common.ResponseBase;
import com.likuncheng.luckdraw.pay.entity.PayBean;
import com.likuncheng.luckdraw.pay.mapper.CoreMapper;
import com.likuncheng.luckdraw.pay.serverImpl.CoreServerImpl;

public class UpdatePayLog implements Callable<String> {
	
	private CoreServerImpl coreServerImpl;
	
	private CoreMapper coreMapper;
	
	private String outTradeNo;
	
	private String tradeNo;
	
	public UpdatePayLog(CoreServerImpl coreServerImpl,String outTradeNo,String tradeNo,CoreMapper coreMapper) {
		this.coreServerImpl = coreServerImpl;
		this.outTradeNo = outTradeNo;
		this.tradeNo = tradeNo;
		this.coreMapper = coreMapper;
	}
	

	@Override
	public String call() throws Exception {
		updatePayLog();
		return "success";
	}

	@Transactional
	public void updatePayLog() throws Exception {
		try {
			ResponseBase payBeanByOutTradeNo = coreServerImpl.getPayBeanByOutTradeNo(outTradeNo);
			if (payBeanByOutTradeNo.getRtnCode() == 500) {
				throw new Exception(payBeanByOutTradeNo.getMsg());
			}
			PayBean payBean = (PayBean) payBeanByOutTradeNo.getData();
			payBean.setTradeNo(tradeNo);
			payBean.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date()));
			Integer updatePayLog = coreMapper.updatePayLog(payBean);
			if (updatePayLog <= 0) {
				throw new Exception("修改支付记录失败");
			}
		} catch (Exception e) {
			throw e;
		}
	}
}
