package com.likuncheng.order.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.likuncheng.order.entity.Order;

@Mapper
public interface OrderMapper {
	
	/**
	 * 创建一个订单
	 * @param order 订单对象
	 * @return 返回数据库标识
	 */
	public Integer createOrder(Order order);
	
	
	/**
	 * 修改支付状态 根据订单id和版本号
	 * @param order
	 * @return 受影响的行数
	 */
	public Integer updatePsyState(Order order);
	
	
	/**
	 * 根据订单号查询订单
	 * @param orderNumber
	 * @return 订单信息
	 */
	public Order getOrderByOrderNumber(@Param("orderNumber")String orderNumber);
	
	/**
	 * 判断该账号是否存在没有支付的订单信息
	 * @param accountNumber 账号信息
	 * @return 订单信息
	 */
	public Order getOrderByAccountNumberAndPayState(@Param("accountNumber")String accountNumber);

}
