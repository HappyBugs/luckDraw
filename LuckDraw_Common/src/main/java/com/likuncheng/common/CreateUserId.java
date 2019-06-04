package com.likuncheng.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateUserId {
	
	private static final String SUO = "";
	
	public static String createUserId() {
		synchronized (SUO) {
			Date date = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String userId = "USER-"+sf.format(date)+"-"+System.currentTimeMillis();
			return userId;
		}
	}

}
