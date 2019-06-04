package com.likuncheng.core.serverImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.likuncheng.common.BaseApiService;
import com.likuncheng.common.ResponseBase;
import com.likuncheng.core.entity.LuckDrawLog;
import com.likuncheng.core.mapper.LuckDrawLogMapper;
import com.likuncheng.core.server.LuckDrawLogServer;

@Service
public class LuckDrawLogServerImpl extends BaseApiService implements LuckDrawLogServer{
	
	@Autowired
	private LuckDrawLogMapper luckDrawLogMapper;

	@Override
	@Transactional
	public ResponseBase createLog(LuckDrawLog luckDrawLog) {
		Integer createLog = luckDrawLogMapper.createLog(luckDrawLog);
		if(createLog<=0) {
			return setResultError("创建抽奖记录失败");
		}
		return setResultSuccess("创建抽奖记录成功");
	}

	@Override
	public ResponseBase getCommoditys( String accountNumber) {
		List<LuckDrawLog> commoditys = luckDrawLogMapper.getCommoditys(accountNumber);
		if(commoditys == null || commoditys.size() == 0) {
			return setResultError("未查找到用户的抽奖商品");
		}
		String jsonString = JSON.toJSONString(commoditys);
		return setResultSuccessData(jsonString, "用户查找商品成功");
	}

	@Override
	@Transactional
	public ResponseBase deleteLuckdrawLog(Integer lId) {
		Integer deleteLuckdrawLog = luckDrawLogMapper.deleteLuckdrawLog(lId.toString());
		if(deleteLuckdrawLog<=0) {
			return setResultError("删除抽奖商品记录失败");
		}
		return setResultSuccess("删除抽奖记录成功");
	}

}
