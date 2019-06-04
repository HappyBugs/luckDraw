package com.likuncheng.luckdraw.pay.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.likuncheng.common.ResponseBase;


@FeignClient("LuckDrawOrder")
@Service
@RequestMapping(value = "/orderServer",method=RequestMethod.POST)
public interface OrderFeign {

	/**
	 * 修改支付状态 根据订单id和版本号
	 * 
	 * @param orderNumber
	 * @return 受影响的行数
	 */
	@RequestMapping(value = "/updatePsyState",method=RequestMethod.POST)
	public ResponseBase updatePsyState(@RequestParam(value = "orderJson") String orderJson);

	/**
	 * 根据订单号查询订单
	 * 
	 * @param orderNumber
	 * @return 订单信息
	 */
	@RequestMapping(value = "/getOrderByOrderNumber",method=RequestMethod.POST)
	public ResponseBase getOrderByOrderNumber(@RequestParam(value = "orderNumber") String orderNumber);
	
}
