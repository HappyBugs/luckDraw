package com.likuncheng.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.likuncheng.core.entity.Commodity;

@Mapper
public interface CommodityMapper {
	
	/**
	 * 创建商品
	 * @param commodity
	 * @return 数据库标识
	 */
	public Integer createCommodit(Commodity commodity);
	
	
	/**
	 * 得到所有的商品
	 * @return
	 */
	public List<Commodity> getCommoditys();
	
	/**
	 * 得到商品积分
	 * @param commodityNumber
	 * @return
	 */
	public String getExchangeByCommodityNumber(@Param("commodityNumber")String commodityNumber);
	

}
