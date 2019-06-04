package com.likuncheng.core.entity;

import lombok.Data;

//抽奖记录
@Data
public class LuckDrawLog {
	
	//数据库标识
	private Integer lId;
	//创建时间
	private String createTime;
	//用户账号
	private String accountNumber;
	//商品信息(编号
	private String commodityNumber;
	//版本
	private Integer version;
	
	public LuckDrawLog() {}
	
	public LuckDrawLog(String createTime,String accountNumber,String commodityNumber) {
		this.createTime = createTime;
		this.accountNumber = accountNumber;
		this.commodityNumber = commodityNumber;
	}

}
