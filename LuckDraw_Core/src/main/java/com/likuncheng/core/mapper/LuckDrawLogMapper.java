package com.likuncheng.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.likuncheng.core.entity.LuckDrawLog;

@Mapper
public interface LuckDrawLogMapper {

	/**
	 * 创建抽奖记录
	 * 
	 * @param luckDrawLog
	 * @return 数据库标识
	 */
	public Integer createLog(LuckDrawLog luckDrawLog);

	/**
	 * 得到所有的商品
	 * 
	 * @return
	 */
	public List<LuckDrawLog> getCommoditys(@Param("accountNumber") String accountNumber);

	/**
	 * 根据id删除记录
	 * 
	 * @param lId
	 * @return
	 */
	public Integer deleteLuckdrawLog(@Param("lId") String lId);

}
