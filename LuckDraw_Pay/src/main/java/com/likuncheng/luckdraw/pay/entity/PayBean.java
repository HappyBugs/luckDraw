package com.likuncheng.luckdraw.pay.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

@Data
public class PayBean {

	// 数据库标识
	private Integer id;
	// 数据库订单id
	private String outTradeNo;
	// 支付金额
	private String totalAmount;
	// 支付标题
	private String subject;
	// 支付描述
	private String body;
	// 支付宝交易号
	private String tradeNo;
	// 创建的时间
	private String createTime;
	//更改时间
	private String updateTime;
	//版本号
	private Integer version;

	public PayBean() {
	}

	public PayBean(String outTradeNo, String totalAmount, String subject, String body) {
		this.outTradeNo = outTradeNo;
		this.totalAmount = totalAmount;
		this.subject = subject;
		this.body = body;
		this.createTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
	}

}
