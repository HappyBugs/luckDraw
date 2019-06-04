package com.likuncheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;

//SSO系统启动
@SpringBootApplication
@EnableDistributedTransaction
@EnableEurekaClient
public class LuckDrawSSOApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(LuckDrawSSOApplication.class, args);
	}
	

}
