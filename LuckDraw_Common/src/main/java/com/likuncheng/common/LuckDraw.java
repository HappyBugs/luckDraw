package com.likuncheng.common;

import java.util.Random;

public class LuckDraw {
	
	private static Random random = new Random();
	
	public static Integer getNumber() {
		Integer number = (random.nextInt(1000)+1);
		return number;
	}
	
	public static String createCommoditNumber() {
		StringBuffer stringBuffer = new StringBuffer("cNumber-");
		for(int i = 0 ; i <= 12 ; i++) {
			stringBuffer.append(random.nextInt(10));
		}
		String commodityNumber = stringBuffer.toString();
		return commodityNumber;
	}
	
}
