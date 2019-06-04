package com.likuncheng.core.serverImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import com.likuncheng.common.BaseApiService;
import com.likuncheng.common.ResponseBase;
import com.likuncheng.core.entity.GoldKey;
import com.likuncheng.core.mapper.GoldKeyMapper;
import com.likuncheng.core.server.GoldKeyServer;

@Service
public class GoldKeyServerImpl extends BaseApiService implements GoldKeyServer {
	
	@Autowired
	private GoldKeyMapper goldKeyMapper;

	@Override
	public ResponseBase getAccountNumber(String accountNumber) {
		String userAccountNumber = goldKeyMapper.getAccountNumber(accountNumber);
		if(StringUtils.isEmpty(userAccountNumber)) {
			return setResultError("查找用户账号失败");
		}
		return setResultSuccessData(userAccountNumber, "查找用户账号成功");
	}


	@Override
	public ResponseBase getGoldKeyByAccountNumber(String accountNumber) {
		GoldKey goldKeyByAccountNumber = goldKeyMapper.getGoldKeyByAccountNumber(accountNumber);
		if(goldKeyByAccountNumber == null) {
			return setResultError("查找对象失败");
		}
		String jsonString = JSON.toJSONString(goldKeyByAccountNumber);
		return setResultSuccessData(jsonString, "查找对象成功");
	}


	@Override
	@Transactional
	@TxcTransaction(propagation=DTXPropagation.SUPPORTS)
	public ResponseBase updateGoldKey(String goldKey) {
		GoldKey parseObject = JSON.parseObject(goldKey,GoldKey.class);
		Integer updateGoldKey = goldKeyMapper.updateGoldKey(parseObject);
		if(updateGoldKey <= 0) {
			return setResultError("修改钥匙数量失败");
		}
		return setResultSuccess("修改钥匙数量成功");
	}


	@Override
	@Transactional
	@TxcTransaction(propagation=DTXPropagation.SUPPORTS)
	public ResponseBase createGoldKey(String accountNumber, String keyNumber) {
		GoldKey goldKey = new GoldKey(accountNumber,Integer.parseInt(keyNumber));
		Integer createGoldKey = goldKeyMapper.createGoldKey(goldKey);
		if(createGoldKey <= 0) {
			return setResultError("创建钥匙失败");
		}
		return setResultSuccess("创建钥匙成功");
	}


	@Override
	@Transactional
	public ResponseBase addIntegral(GoldKey goldKey) {
		Integer addIntegral = goldKeyMapper.addIntegral(goldKey);
		if(addIntegral <= 0) {
			return setResultError("增加积分失败");
		}
		return setResultSuccess("增加积分成功");
	}


	@Override
	@Transactional
	public ResponseBase reduceIntegral(GoldKey goldKey) {
		Integer reduceIntegral = goldKeyMapper.reduceIntegral(goldKey);
		if(reduceIntegral <= 0) {
			return setResultError("减少积分失败");
		}
		return setResultSuccess("减少积分成功");
	}

}
