package com.likuncheng.core.feigns;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.likuncheng.common.ResponseBase;

@FeignClient(value = "LuckDrawPay")
@Service
@RequestMapping(value = "/aliPay")
public interface PayFeign {

	@RequestMapping(value = "payServer", method = RequestMethod.POST)
	public ResponseBase payServer(@RequestParam(value = "payToken") String payToken);
	
//	@GetMapping(value = "payReturn")
//	public ResponseBase payReturn(HttpServletRequest request, HttpServletResponse response);
}
