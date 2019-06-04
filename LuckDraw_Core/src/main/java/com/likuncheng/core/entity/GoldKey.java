package com.likuncheng.core.entity;

import lombok.Data;

//金钥匙
@Data
public class GoldKey {
	
	//数据库标识
	private Integer gId;
	//用户账号
	private String accountNumber;
	//钥匙数量
	private Integer keyNumber;
	//幸运积分
	private Integer integral;
	//已经抽奖次数
	private Integer luckNumber;
	//版本号
	private Integer version;
	
	public GoldKey() {}
	
	public GoldKey(String accountNumber,Integer keyNumber) {
		this.accountNumber = accountNumber;
		this.keyNumber = keyNumber;
	}
	
	
	
	

}
