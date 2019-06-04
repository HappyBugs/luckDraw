//package com.likuncheng.core.Interceptor;
//
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.alibaba.druid.util.StringUtils;
//import com.likuncheng.common.BaseRedisService;
//import com.likuncheng.core.feigns.SSOFeign;
//
//@Component
//public class MyInterceptor implements HandlerInterceptor {
//	
//	@Autowired
//	private StringRedisTemplate stringRedisTemplate;
//	
//	@Autowired
//	private SSOFeign ssoFeign;
//	
//	// 请求之前进行拦截
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//			throws Exception {
//		String header = request.getHeader("loginToken");
//		if(StringUtils.isEmpty(header)) {
//			request.setAttribute("errorMsg", "请登录");
//			ssoFeign.testGoLogin();
//			return false;
//		}
//		String string = BaseRedisService.getString(header, stringRedisTemplate);
//		if(StringUtils.isEmpty(string)) {
//			request.setAttribute("errorMsg", "登陆信息已过期");
//			ssoFeign.testGoLogin();
//			return false;
//		}
//		System.out.println("登陆成功："+request.getRequestURL()+"?"+request.getQueryString());
//		return true;// true 放行
//	}
//
//	@Override
//	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//			ModelAndView modelAndView) throws Exception {
//		// TODO Auto-generated method stub
//
//	}
//
//	// 请求之后进行拦截
//	@Override
//	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
//			throws Exception {
//
//		System.out.println("进入拦截器之后调用...............");
//
//	}
//
//}
