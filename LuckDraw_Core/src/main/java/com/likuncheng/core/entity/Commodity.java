package com.likuncheng.core.entity;

import lombok.Data;

@Data
public class Commodity {
	
	//数据库标识
	private Integer cId;
	//概率
	private String probability;
	//商品编号
	private String commodityNumber;
	//兑换积分
	private Integer exchange;
	//商品名称
	private String commodityName;
	//商品描述
	private String commodityDescribe;
	//图片地址
	private String logAddress;
	//分解积分
	private Integer decompose;
	//版本号 默认0
	private Integer version;
	
	public Commodity() {}
	
	public Commodity(String commodityNumber,String commodityName,String probability,Integer exchange,Integer decompose) {
		this.commodityNumber = commodityNumber;
		this.commodityName = commodityName;
		this.probability = probability;
		this.exchange = exchange;
		this.decompose = decompose;
	}
	

}
