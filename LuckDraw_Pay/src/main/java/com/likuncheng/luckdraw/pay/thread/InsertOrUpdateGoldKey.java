package com.likuncheng.luckdraw.pay.thread;

import java.util.concurrent.Callable;

import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.likuncheng.common.ResponseBase;
import com.likuncheng.luckdraw.pay.feign.GoldKeyFeign;

public class InsertOrUpdateGoldKey implements Callable<String> {
	
	private GoldKeyFeign goldKeyFeign;
	
	private JSONObject parseObject;
	
	public InsertOrUpdateGoldKey(GoldKeyFeign goldKeyFeign,JSONObject parseObject) {
		this.goldKeyFeign = goldKeyFeign;
		this.parseObject = parseObject;
	}

	@Override
	public String call() throws Exception {
		insertOrUpdateGoldKey();
		return "success";
	}
	
	@Transactional
	public void insertOrUpdateGoldKey() throws Exception {
		try {
			Integer type = parseObject.getInteger("type");
			Integer keyNumber = type == 0 ? 1 : 10;
			String accountNumber = parseObject.getString("accountNumber");
			ResponseBase goldKeyByAccountNumber = goldKeyFeign.getGoldKeyByAccountNumber(accountNumber);
			if (goldKeyByAccountNumber.getRtnCode() == 500) {
				ResponseBase createGoldKey = goldKeyFeign.createGoldKey(accountNumber, keyNumber.toString());
				if (createGoldKey.getRtnCode() == 200) {
				}
				throw new Exception("创建钥匙失败");
			}
			String goldKeyJson = goldKeyByAccountNumber.getData().toString();
			JSONObject goldKeyJSON = JSON.parseObject(goldKeyJson);
			Integer newKeyNumber = goldKeyJSON.getInteger("keyNumber");
			goldKeyJSON.put("keyNumber", (keyNumber + newKeyNumber));
			String newGoldKeyJson = goldKeyJSON.toJSONString();
			ResponseBase updateGoldKey = goldKeyFeign.updateGoldKey(newGoldKeyJson);
			if (updateGoldKey.getRtnCode() == 200) {
				throw new Exception("增加钥匙失败");
			}
		} catch (Exception e) {
			throw e;
		}
	}

}
