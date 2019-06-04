package com.likuncheng.sso.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.likuncheng.common.BaseApiService;
import com.likuncheng.common.BaseRedisService;
import com.likuncheng.common.CreateLoginToken;
import com.likuncheng.common.ResponseBase;
import com.likuncheng.sso.entity.JWTTokenUtil;
import com.likuncheng.sso.entity.User;
import com.likuncheng.sso.server.SSOServer;

@Controller
@RequestMapping(value = "/sso")
public class SSOController extends BaseApiService {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private SSOServer ssoServer;

	@GetMapping(value = "/goRegister")
	public String goRegister(HttpServletRequest request) {
		User user = new User();
		// 用于给用户展示账号
		request.setAttribute("accountNumber", user.getAccountNumber());
		return "注册页面地址";
	}

	@PostMapping(value = "/register")
	@ResponseBody
	public Object register(User user, HttpServletRequest request) throws Exception {
		String newPassWord = JWTTokenUtil.createJWT(user.getPassWord());
		user.setPassWord(newPassWord);
		ResponseBase register = ssoServer.register(user);
		if (register.getRtnCode() == 500) {
			request.setAttribute("error", "注册失败");
			return "返回注册页面";
		}
		// 用于登陆页面展示账号
		request.setAttribute("yes", user.getAccountNumber());
		return "返回页面 并提示登录";
	}

	@GetMapping(value = "/goLogin")
	public String goLogin() {

		return "登陆页面地址";
	}

	@PostMapping(value = "/login")
	public String login(String accountNumber, String passWord, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			if (StringUtils.isEmpty(accountNumber) || StringUtils.isEmpty(passWord)) {
				throw new Exception("账号密码不能为空");
			}
			String newPassWord = JWTTokenUtil.createJWT(passWord);
			ResponseBase login = ssoServer.login(accountNumber, newPassWord);
			if (login.getRtnCode() == 500) {
				throw new Exception("用户登陆失败" + login.getMsg());
			}
			String userAccountNumber = (String) login.getData();
			// 标识已经登录
			BaseRedisService.setString(userAccountNumber, "yes", stringRedisTemplate,1*60*30);
			// 保存到客户端 Authorization 自己的账号
			response.setHeader("Authorization", userAccountNumber);
		} catch (Exception e) {
			stringRedisTemplate.discard();
			request.setAttribute("loginError", e.getMessage());
			return "登陆页面";
		} finally {
			stringRedisTemplate.exec();
		}
		return "返回首页";
	}

	// 测试注册页面 高并发测试通过
	@PostMapping(value = "/testRegister")
	@ResponseBody
	public Object testRegister(String passWord) {
		synchronized (ssoServer) {
			User user = new User();
			String newPassWord = JWTTokenUtil.createJWT(passWord);
			user.setPassWord(newPassWord);
			ResponseBase register = ssoServer.register(user);
			if (register.getRtnCode() == 500) {
				return register;
			}
			register.setData(user);
			return register;
		}
	}

	// 201标识标识用户未登录 202标识用户已经登陆 跳转到相应的地址
	@RequestMapping(value = "/testGoLogin")
	@ResponseBody
	public ResponseBase testGoLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String requestPath = (String) request.getAttribute("requestPath");
		System.out.println("需要相应的地址为：" + requestPath);
		// 得到所有的参数
		Map<String, String[]> parameterMap = request.getParameterMap();
		// 得到payToken
		String[] strings = parameterMap.get("loginToken");
		// 如果所有的值为null 或者没有值 就是 没有登陆
		if (strings == null || strings.length == 0) {
			// 如果该参数不为空 就代表是其他页面发过来需要登录请求的
			if (!StringUtils.isEmpty(requestPath)) {
				request.setAttribute("requestPath", requestPath);
			}
			return setResult(201, "用户未登录", "登录页面");
		}
		//表示已经登录 直接从定向到 responsePath
		return setResult(202, "您经登录", requestPath);
	}

	// 因为需要测试 所以加上@ResponseBody返回字符串
	@RequestMapping(value = "/testLogin")
	@ResponseBody
	@Transactional
	public ResponseBase testLogin(String accountNumber, String passWord, HttpServletRequest request,
			HttpServletResponse response) {
		String result = "";
		try {
			if (StringUtils.isEmpty(accountNumber) || StringUtils.isEmpty(passWord)) {
				return setResultError("账号密码不能为空");
			}
			String newPassWord = JWTTokenUtil.createJWT(passWord);
			ResponseBase login = ssoServer.login(accountNumber, newPassWord);
			if (login.getRtnCode() == 500) {
				return setResultError("用户登陆失败," + login.getMsg());
			}
			// 账号
			String userAccountNumber = (String) login.getData();
			// 生成登录token
			String loginToken = CreateLoginToken.createLoginToken();
			// 标识已经登录
			BaseRedisService.setString(loginToken, userAccountNumber, stringRedisTemplate,1*60*30);
			String requestPath = (String) request.getAttribute("requestPath");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("loginToken", loginToken);
			jsonObject.put("userAccountNumber", userAccountNumber);
			if(! StringUtils.isEmpty(requestPath)) {
				jsonObject.put("requestPath", requestPath);
			}else {
				jsonObject.put("requestPath", "首页地址");
			}
			result = jsonObject.toJSONString();
		} catch (Exception e) {
			stringRedisTemplate.discard();
			return setResultError("登陆失败,系统异常:" + e.getMessage());
		}
		stringRedisTemplate.exec();
		return setResultSuccessData(result, "用户登录成功");
	}

}
