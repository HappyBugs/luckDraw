package com.likuncheng.luckdraw.pay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;

@SpringBootApplication
@EnableDistributedTransaction
@EnableFeignClients
@EnableEurekaClient
@EnableAspectJAutoProxy(exposeProxy=true)
public class PayApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PayApplication.class, args);
	}

}
