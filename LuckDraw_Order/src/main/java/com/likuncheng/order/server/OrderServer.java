package com.likuncheng.order.server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.likuncheng.common.ResponseBase;

@RequestMapping(value = "/orderServer", method = RequestMethod.POST)
public interface OrderServer {

	/**
	 * 创建订单
	 * 
	 * @param accountNumber 用户账号
	 * @param orderDescribe 支付描述
	 * @param money         支付金额
	 * @param type          购买类型
	 * @return 返回封装结果
	 */
	@RequestMapping(value = "/createOrder", method = RequestMethod.POST)
	public ResponseBase createOrder(@RequestParam(value = "accountNumber") String accountNumber,
			@RequestParam(value = "type") String type,@RequestParam(value = "payToken") String payToken);

	/**
	 * 修改支付状态 根据订单id和版本号
	 * 
	 * @param orderNumber
	 * @return 受影响的行数
	 */
	@RequestMapping(value = "/updatePsyState", method = RequestMethod.POST)
	public ResponseBase updatePsyState(@RequestParam(value = "orderJson") String orderJson);

	/**
	 * 根据订单号查询订单
	 * 
	 * @param orderNumber
	 * @return 订单信息
	 */
	@RequestMapping(value = "/getOrderByOrderNumber", method = RequestMethod.POST)
	public ResponseBase getOrderByOrderNumber(@RequestParam(value = "orderNumber") String orderNumber);

	/**
	 * 判断该账号是否存在没有支付的订单信息
	 * @param accountNumber 账号信息
	 * @return 订单信息
	 */
	@RequestMapping(value = "/getOrderByAccountNumberAndPayState", method = RequestMethod.POST)
	public ResponseBase getOrderByAccountNumberAndPayState(@RequestParam(value="accountNumber")String accountNumber);

}
