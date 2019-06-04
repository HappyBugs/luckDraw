package com.likuncheng.sso.serverImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.likuncheng.common.BaseApiService;
import com.likuncheng.common.ResponseBase;
import com.likuncheng.sso.entity.User;
import com.likuncheng.sso.mapper.SSOMapper;
import com.likuncheng.sso.server.SSOServer;

@Service
public class SSOServerImpl extends BaseApiService implements SSOServer {
	
	@Autowired
	private SSOMapper ssoMapper;

	@Override
	public ResponseBase login(String accountNumber, String passWord) {
		ResponseBase result = this.getAccountNumber(accountNumber);
		boolean isExistence = (boolean) result.getData();
		if(! isExistence) {
			return setResultError("请输入正确的账号!");
		}
		//用户账号
		String userAccountNumber = ssoMapper.login(accountNumber, passWord);
		if(StringUtils.isEmpty(userAccountNumber)) {
			return setResultError("用户登陆失败,请输入正确的账号密码!");
		}
		return setResultSuccessData(userAccountNumber, "登录成功");
	}

	@Transactional
	@Override
	public ResponseBase register(User user) {
		Integer register = ssoMapper.register(user);
		if(register <= 0) {
			return setResultError("注册用户失败!");
		}
		return setResultSuccess("注册用户成功");
	}

	@Override
	public ResponseBase getAccountNumber(String accountNumber) {
		String newAccountNumber = ssoMapper.getAccountNumber(accountNumber);
		if(StringUtils.isEmpty(newAccountNumber)) {
			return setResultSuccessData(false, "没有该账号");
		}
		return setResultSuccessData(true, "查找账号成功");
	}

}
