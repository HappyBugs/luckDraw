package com.likuncheng.core.serverImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.likuncheng.common.BaseApiService;
import com.likuncheng.common.ResponseBase;
import com.likuncheng.core.entity.Commodity;
import com.likuncheng.core.mapper.CommodityMapper;
import com.likuncheng.core.server.CommodityServer;

/**
 * 实现类
 * @author Administrator
 *
 */
@Service
public class CommodityServerImpl extends BaseApiService implements CommodityServer {
	
	@Autowired
	private CommodityMapper commodityMapper;

	/**
	 * 创建商品
	 */
	@Override
	@Transactional
	public ResponseBase createCommodit(Commodity commodity) {
		//调用方法 创建商品
		Integer createCommodit = commodityMapper.createCommodit(commodity);
		if(createCommodit<=0) {
			return setResultError("创建商品失败");
		}
		//返回结果
		return setResultSuccess("创建商品成功");
	}

	/**
	 * 得到所有的商品信息
	 */
	@Override
	public ResponseBase getCommoditys() {
		//调用方法获得所有的商品
		List<Commodity> commodityNumbers = commodityMapper.getCommoditys();
		if(commodityMapper == null || commodityNumbers.size() <= 0) {
			return setResultError("没有查找到商品信息");
		}
		//返回结果
		return setResultSuccessData(commodityNumbers, "查找商品信息成功");
	}

	/**
	 * 根据商品编号进行查询商品积分
	 */
	@Override
	public ResponseBase getExchangeByCommodityNumber(String commodityNumber) {
		//调用方法进行查询
		String exchangeByCommodityNumber = commodityMapper.getExchangeByCommodityNumber(commodityNumber);
		if(StringUtils.isEmpty(exchangeByCommodityNumber)) {
			return setResultError("未查找到对应的分解积分");
		}
		//返回结果
		return setResultSuccessData(exchangeByCommodityNumber, "成功查找到分解积分");
	}

}
