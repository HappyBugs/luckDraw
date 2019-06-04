package com.likuncheng.core.server;



import com.likuncheng.common.ResponseBase;
import com.likuncheng.core.entity.LuckDrawLog;

public interface LuckDrawLogServer {
	

	/**
	 * 创建抽奖记录
	 * @param luckDrawLog
	 * @return 数据库标识
	 */
	public ResponseBase createLog(LuckDrawLog luckDrawLog);
	
	/**
	 * 得到所有的商品
	 * @return
	 */
	public ResponseBase getCommoditys( String accountNumber);
	
	/**
	  * 根据id删除记录
	 * @param lId
	 * @return
	 */
	public ResponseBase deleteLuckdrawLog(Integer lId);
	
	
}
