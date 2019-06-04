package com.likuncheng.core.server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.likuncheng.common.ResponseBase;
import com.likuncheng.core.entity.GoldKey;

@RequestMapping(value = "/GoldKeyServer", method = RequestMethod.POST)
public interface GoldKeyServer {

	/**
	 * 判断数据库是否存在
	 * 
	 * @param accountNumber
	 * @return 返回查找到的id
	 */
	public ResponseBase getAccountNumber(String accountNumber);

	/**
	 * 对金钥匙数据库的更新 比如钥匙的加减和积分的加减,抽奖次数的加减
	 * 
	 * @param goldKey
	 * @return 是否成功 影响的行数
	 */
	@RequestMapping(value = "/updateGoldKey", method = RequestMethod.POST)
	public ResponseBase updateGoldKey(@RequestParam(value = "goldKey") String goldKey);

	/**
	 * 根据用户账号 查询金钥匙对象
	 * 
	 * @param accountNumber 用户账号
	 * @return 返回金钥匙对象json格式
	 */
	@RequestMapping(value = "/getGoldKeyByAccountNumber", method = RequestMethod.POST)
	public ResponseBase getGoldKeyByAccountNumber(@RequestParam(value = "accountNumber") String accountNumber);

	/**
	 * 增加金钥匙
	 * 
	 * @param goldKey
	 * @return 数据库标识
	 */
	@RequestMapping(value = "/createGoldKey", method = RequestMethod.POST)
	public ResponseBase createGoldKey(@RequestParam(value = "accountNumber") String accountNumber,
			@RequestParam(value = "keyNumber") String keyNumber);

	/**
	 * 增加积分
	 * 
	 * @param integral
	 * @param accountNumber
	 * @return
	 */
	public ResponseBase addIntegral(GoldKey goldKey);

	/**
	 * 减少积分
	 * 
	 * @param integral
	 * @param accountNumber
	 * @return
	 */
	public ResponseBase reduceIntegral(GoldKey goldKey);

}
