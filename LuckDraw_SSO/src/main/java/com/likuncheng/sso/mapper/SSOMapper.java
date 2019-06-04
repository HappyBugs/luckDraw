package com.likuncheng.sso.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.likuncheng.sso.entity.User;

@Mapper
public interface SSOMapper {
	
	/**
	 * 用户登录
	 * @param accountNumber 账号
	 * @param passWord 密码
	 * @return 用户账号
	 */
	public String login(@Param("accountNumber")String accountNumber,@Param("passWord")String passWord); 
	
	
	/**
	 * 判断数据库是否存在该账号
	 * @param accountNumber 账号
	 * @return 账号
	 */
	public String getAccountNumber(@Param("accountNumber")String accountNumber);
	
	
	/**
	 * 用户注册
	 * @param user 用户注册需要的对象
	 * @return 用户数据库唯一标识
	 */
	public Integer register(User user);
	
	//修改昵称
	//修改密码
	//注销

}
