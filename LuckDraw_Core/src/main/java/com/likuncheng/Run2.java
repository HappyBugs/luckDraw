package com.likuncheng;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.likuncheng.common.ResponseBase;
import com.likuncheng.core.entity.Commodity;
import com.likuncheng.core.server.CommodityServer;

//用于获取参与抽奖的商品 并保存到集合中 方便后面后面抽奖获取商品信息
@Component
public class Run2 implements ApplicationRunner{
	
	//保存用户信息
	public static List<Commodity> sommoditys = new ArrayList<>();
	
	@Autowired
	private CommodityServer commodityServer;

	@SuppressWarnings("unchecked")
	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("开始执行======================>");
		ResponseBase commoditys = commodityServer.getCommoditys();
		System.out.println(commoditys.getMsg());
		sommoditys = (List<Commodity>) commoditys.getData();
		System.out.println("执行结束======================>");
	}

}
