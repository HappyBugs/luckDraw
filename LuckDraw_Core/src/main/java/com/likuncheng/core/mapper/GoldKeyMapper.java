package com.likuncheng.core.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.likuncheng.core.entity.GoldKey;

@Mapper
public interface GoldKeyMapper {

	/**
	 * 增加金钥匙
	 * 
	 * @param goldKey
	 * @return 数据库标识
	 */
	public Integer createGoldKey(GoldKey goldKey);

	/**
	 * 判断数据库是否存在
	 * 
	 * @param accountNumber
	 * @return 返回查找到的id
	 */
	public String getAccountNumber(@Param("accountNumber") String accountNumber);

	/**
	 * 对金钥匙数据库的更新 比如钥匙的加减和积分的加减,抽奖次数的加减
	 * 
	 * @param goldKey
	 * @return 是否成功 影响的行数
	 */
	public Integer updateGoldKey(GoldKey goldKey);

	/**
	 * 根据用户账号 查询金钥匙对象
	 * 
	 * @param accountNumber 用户账号
	 * @return 返回金钥匙对象
	 */
	public GoldKey getGoldKeyByAccountNumber(@Param("accountNumber") String accountNumber);

	/**
	 * 增加积分
	 * 
	 * @param integral
	 * @param accountNumber
	 * @return
	 */
	public Integer addIntegral(GoldKey goldKey);

	/**
	 * 减少积分
	 * 
	 * @param integral
	 * @param accountNumber
	 * @return
	 */
	public Integer reduceIntegral(GoldKey goldKey);

}
