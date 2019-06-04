package com.likuncheng.core.entity;

import com.likuncheng.Run2;
import com.likuncheng.common.LuckDraw;

public class GetCommoditNumberUtils {
	
	public static Commodity getCommodityNumber() throws Exception {
		//产生的随机数
		Commodity result = null;
		Integer number = LuckDraw.getNumber();
		for (Commodity commodity : Run2.sommoditys) {
			String probability = commodity.getProbability();
			String[] split = probability.split("~");
			//大于第一个数字 小于第二个数据 那就是这个区间的
			if(number >= Integer.parseInt(split[0]) && number <= Integer.parseInt(split[1])) {
				result =  commodity;
				break;
			}
		}
		if(result == null) {
			throw new Exception("没有该商品");
		}
		System.err.println("商品信息:"+result);
		return result;
	}

}
