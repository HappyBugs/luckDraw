package com.likuncheng.order.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.likuncheng.common.CreateOrderId;

import lombok.Data;

@Data
public class Order {

	// 订单id
	private Integer oId;
	// 订单号
	private String orderNumber;
	// 用户账号
	private String accountNumber;
	// 订单描述
	private String orderDescribe;
	// 创建时间
	private String createTime;
	// 支付状态 0 未支付 1 已支付 2异常金额
	private Integer payState;
	// 订单金额
	private String money;
	// 订单类型 0 一个金钥匙 10个金钥匙
	private Integer type;
	// 更新时间
	private String updateTime;
	// 版本号
	private Integer version;
	//支付token
	private String payToken;

	public Order() {
	}

	public Order(Integer oId, String orderNumber, String accountNumber, String orderDescribe, String createTime,
			Integer payState, String money, Integer type, String updateTime, Integer version,String payToken) {
		this.oId = oId;
		this.orderNumber = orderNumber;
		this.accountNumber = accountNumber;
		this.orderDescribe = orderDescribe;
		this.createTime = createTime;
		this.payState = payState;
		this.money = money;
		this.type = type;
		this.updateTime = updateTime;
		this.version = version;
		this.payToken = payToken;

	}

	public Order(String accountNumber, String type,String payToken) {
		this.accountNumber = accountNumber;
		this.type = Integer.parseInt(type);
		this.orderNumber = CreateOrderId.createOrderId();
		this.createTime = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
		this.payToken = payToken;
		if (this.type == 0) {
			this.money = "10";
			this.orderDescribe = "购买一个金钥匙";
		}
		if (this.type == 1) {
			this.money = "100";
			this.orderDescribe = "购买十个金钥匙";
		}
	}

}
