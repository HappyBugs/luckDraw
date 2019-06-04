package com.likuncheng.core.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.likuncheng.common.ResponseBase;

//调用抽奖的服务
public interface CoreServer {

	/**
	 * 用户抽奖
	 * 
	 * @return 返回抽到的商品信息
	 */
	public ResponseBase luckDraw(String accountNumber, Integer type);

	/**
	 * 购买钥匙
	 * 
	 * @param accountNumber
	 * @param type
	 * @return
	 */
	public ResponseBase purchaseKey(String accountNumber, Integer type, HttpServletRequest request,
			HttpServletResponse response)throws Exception;
	
	/**
	  * 分解商品
	 * @param commodityJson
	 * @return
	 */
	public ResponseBase decompose(String commodityJson);
	
	
	/**
	 * 查看抽奖到的商品
	 * @param accountNumber
	 * @return
	 */
	public ResponseBase showDecompose(String accountNumber);
	
	/**
	  * 兑换商品
	 * @param accountNumber
	 * @param type
	 * @return
	 */
	public ResponseBase exchange(String accountNumber,String type);

}
