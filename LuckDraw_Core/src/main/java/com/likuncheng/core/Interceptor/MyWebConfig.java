//package com.likuncheng.core.Interceptor;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
//@Configuration
//public class MyWebConfig extends WebMvcConfigurationSupport{
//	
//	@Autowired
//	private MyInterceptor myInterceptor;
//	
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		//拦截器3
//		InterceptorRegistration interceptor3 = registry.addInterceptor(myInterceptor);
//		interceptor3.addPathPatterns("/**");//拦截所有路径
//		interceptor3.excludePathPatterns("/index");//不拦截的路径
//
//	}
//}
