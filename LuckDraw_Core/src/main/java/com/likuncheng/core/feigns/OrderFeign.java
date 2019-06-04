package com.likuncheng.core.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.likuncheng.common.ResponseBase;

@FeignClient(value = "LuckDrawOrder")
@Service
@RequestMapping(value = "/orderServer",method=RequestMethod.POST)
public interface OrderFeign {

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
	 * 判断该账号是否存在没有支付的订单信息
	 * @param accountNumber 账号信息
	 * @return 订单信息
	 */
	@RequestMapping(value = "/getOrderByAccountNumberAndPayState", method = RequestMethod.POST)
	public ResponseBase getOrderByAccountNumberAndPayState(@RequestParam(value="accountNumber")String accountNumber);
	
	/**
	 * 根据订单号查询订单
	 * @param orderNumber
	 * @return 返回的是订单的json格式
	 */
	@RequestMapping(value = "/getOrderByOrderNumber", method = RequestMethod.POST)
	public ResponseBase getOrderByOrderNumber(@RequestParam(value = "orderNumber") String orderNumber);


	

}
