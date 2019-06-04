package com.likuncheng.sso.server;



import com.likuncheng.common.ResponseBase;
import com.likuncheng.sso.entity.User;

public interface SSOServer {
	
	/**
	 * 用户登录
	 * @param accountNumber 账号
	 * @param passWord 密码
	 * @return 封装返回结果
	 */
	public ResponseBase login(String accountNumber,String passWord); 
	
	
	/**
	 * 用户注册
	 * @param user 用户注册需要的对象
	 * @return 封装返回结果
	 */
	public ResponseBase register(User user);
	
	
	/** 
	 * 判断数据库是否存在该账号
	 * @param accountNumber 账号
	 * @return 封装返回结果
	 */
	public ResponseBase getAccountNumber(String accountNumber);
	

}
