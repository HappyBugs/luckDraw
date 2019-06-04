package com.likuncheng.order.serverImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.TxcTransaction;
import com.likuncheng.common.BaseApiService;
import com.likuncheng.common.ResponseBase;
import com.likuncheng.order.entity.Order;
import com.likuncheng.order.mapper.OrderMapper;
import com.likuncheng.order.server.OrderServer;

@Service
@RestController
public class OrderServerImpl extends BaseApiService implements OrderServer {

	@Autowired
	private OrderMapper orderMapper;
	
	//创建order信息  accountNumber：账号信息  type：0，一个金钥匙   1，十个金钥匙，送一个金钥匙，一共十一个  payToken：用于支付
	@Override
	//分布式事务被调用方
	@Transactional
	@TxcTransaction(propagation=DTXPropagation.SUPPORTS)
	public ResponseBase createOrder(String accountNumber,String type,String payToken) {
		//实例化订单对象
		Order order = new Order(accountNumber,type,payToken);
		//创建订单
		Integer createOrder = orderMapper.createOrder(order);
		if(createOrder <= 0) {
			return setResultError("创建订单失败");
		}
		//返回结果
		return setResultSuccessData(order.getOrderNumber(),"创建订单成功");
	}

	//更新支付状态  orderJson 需要更新的order对象的json格式
	@Override
	@Transactional
	@TxcTransaction(propagation=DTXPropagation.SUPPORTS)
	public ResponseBase updatePsyState(String orderJson) {
		System.out.println("接收到的order数据："+orderJson);
		//报获取到的order对象进行类型转换 
		Order order = JSON.parseObject(orderJson, Order.class);
		System.err.println("使用json进行转换的order对象:"+order);
		//更新支付状态
		Integer updatePsyState = orderMapper.updatePsyState(order);
		if(updatePsyState <= 0) {
			return setResultError("修改订单支付状态失败");
		}
		//返回结果
		return setResultSuccess("修改订单支付状态成功");
	}

	//根据账号查询订单信息
	@Override
	public ResponseBase getOrderByOrderNumber(String orderNumber) {
		//调用方法获得订单信息
		Order orderByOrderNumber = orderMapper.getOrderByOrderNumber(orderNumber);
		if(orderByOrderNumber == null) {
			return setResultError("查找订单信息失败");
		}
		//类型转换 进行传递
		String jsonString = JSON.toJSONString(orderByOrderNumber);
		//返回数据
		return setResultSuccessData(jsonString, "查找订单信息成功");
	}

	//根据账号信息查询他没有支付的订单对象 accountNumber：账号
	@Override
	public ResponseBase getOrderByAccountNumberAndPayState(String accountNumber) {
		//获取对象
		Order order = orderMapper.getOrderByAccountNumberAndPayState(accountNumber);
		if(order == null) {
			return setResultError("查找订单信息失败");
		}
		//这里就是标识该账号存在未支付的订单信息
		JSONObject jsonObject = new JSONObject();
		//添加数据
		jsonObject.put("orderNumber", order.getOrderNumber());
		jsonObject.put("payToken", order.getPayToken());
		String jsonString = jsonObject.toJSONString();
		//返回结果
		return setResultSuccessData(jsonString, "查找订单信息成功");
	}

}
