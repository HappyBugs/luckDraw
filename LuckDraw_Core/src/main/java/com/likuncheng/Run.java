//package com.likuncheng;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import com.likuncheng.common.LuckDraw;
//import com.likuncheng.common.ResponseBase;
//import com.likuncheng.core.entity.Commodity;
//import com.likuncheng.core.server.CommodityServer;
//
//@Component
//public class Run implements ApplicationRunner{
//	
//	private static List<Commodity> commoditys = new ArrayList<>();
//	
//	public void addCommodity() {
//		Commodity commodity1 = new Commodity(LuckDraw.createCommoditNumber(),"苹果XMAX","1~2",9999,4500);
//		commoditys.add(commodity1);
//		Commodity commodity2 = new Commodity(LuckDraw.createCommoditNumber(),"IQOO(8+128)","3~5",3200,1600);
//		commoditys.add(commodity2);
//		Commodity commodity3 = new Commodity(LuckDraw.createCommoditNumber(),"Airpods(搭配无线充电盒)","16~11",1600,800);
//		commoditys.add(commodity3);
//		Commodity commodity4 = new Commodity(LuckDraw.createCommoditNumber(),"现金1000元","12~21",900,450);
//		commoditys.add(commodity4);
//		Commodity commodity5 = new Commodity(LuckDraw.createCommoditNumber(),"现金200元","22~51",150,45);
//		commoditys.add(commodity5);
//		Commodity commodity6 = new Commodity(LuckDraw.createCommoditNumber(),"纪念抱枕一个","52~201",50,25);
//		commoditys.add(commodity6);
//		Commodity commodity7 = new Commodity(LuckDraw.createCommoditNumber(),"话费优惠卷50元","202~301",30,15);
//		commoditys.add(commodity7);
//		Commodity commodity8 = new Commodity(LuckDraw.createCommoditNumber(),"话费优惠卷30元","302~501",20,10);
//		commoditys.add(commodity8);
//		Commodity commodity9 = new Commodity(LuckDraw.createCommoditNumber(),"话费优惠卷10元","202~1000",5,3);
//		commoditys.add(commodity9);
//	}
//
//	@Autowired
//	private CommodityServer commodityServer;
//	
//	@Override
//	public void run(ApplicationArguments args) throws Exception {
//		System.out.println("开始执行");
//		addCommodity();
//		for (Commodity commodity : commoditys) {
//			ResponseBase createCommodit = commodityServer.createCommodit(commodity);
//			System.out.println(createCommodit.getMsg());
//		}
//		System.out.println("执行完毕");
//	}
//
//}
