package com.likuncheng.common;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

	public static void addCookie(HttpServletResponse response, String key, String value) {
		try {
			Cookie cookie = new Cookie(key, value);
			cookie.setPath("/");
			//30分钟
			cookie.setMaxAge(1 * 60 * 30);
			response.addCookie(cookie);
		} catch (Exception e) {
			throw e;
		}
	}

	public static Object getCookie(HttpServletRequest request, String key) {
		try {
			Cookie[] cookies = request.getCookies();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(key)) {
					return cookie.getValue();
				}
			}
			return null;
		} catch (Exception e) {
			throw e;
		}
	}

}
