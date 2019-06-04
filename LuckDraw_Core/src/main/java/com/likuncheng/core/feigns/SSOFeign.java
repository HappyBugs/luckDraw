package com.likuncheng.core.feigns;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.likuncheng.common.ResponseBase;

@FeignClient(value = "LuckDrawSSO")
@RequestMapping(value = "/sso")
@Service
public interface SSOFeign {

	/**
 	 * 登录地址 用于用户没有登录 进行调用
	 * @return
	 */
	@RequestMapping(value = "/testGoLogin")
	public ResponseBase testGoLogin();

}
