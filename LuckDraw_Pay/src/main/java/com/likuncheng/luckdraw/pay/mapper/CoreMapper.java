package com.likuncheng.luckdraw.pay.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.likuncheng.luckdraw.pay.entity.PayBean;

@Mapper
public interface CoreMapper {

	// 创建支付记录
	public Integer createPayLog(PayBean payBean);

	// 修改支付记录
	public Integer updatePayLog(PayBean payBean);
	
	//得到支付记录
	public PayBean getPayBeanByOutTradeNo(@Param("outTradeNo")String outTradeNo);

}
