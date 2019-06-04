package com.likuncheng.common;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.druid.util.StringUtils;

public class BaseRedisService {


	public static void setString(String key, String value, StringRedisTemplate stringRedisTemplate,long outTime) {
		try {
			// 开始事务
			stringRedisTemplate.setEnableTransactionSupport(true);
			stringRedisTemplate.multi();
//			stringRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
			if (StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(key)) == false) {
				stringRedisTemplate.opsForValue().setIfPresent(key, value, outTime, TimeUnit.SECONDS);
			} else {
				stringRedisTemplate.opsForValue().set(key, value, outTime, TimeUnit.SECONDS);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static String getString(String key, StringRedisTemplate stringRedisTemplate) {
		String value = stringRedisTemplate.opsForValue().get(key);
		return value;
	}

	public void delKey(String key, StringRedisTemplate stringRedisTemplate) {
		stringRedisTemplate.delete(key);
	}

}
