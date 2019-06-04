package com.likuncheng.core.server;



import com.likuncheng.common.ResponseBase;
import com.likuncheng.core.entity.Commodity;

public interface CommodityServer {
	
	/**
	 * 创建商品
	 * @param commodity
	 * @return 数据库标识
	 */
	public ResponseBase createCommodit(Commodity commodity);
	
	/**
	 * 得到所有的商品
	 * @return
	 */
	public ResponseBase getCommoditys();
	
	/**
	 * 得到商品的分解积分
	 * @param commodityNumber
	 * @return
	 */
	public ResponseBase getExchangeByCommodityNumber(String commodityNumber);

}
