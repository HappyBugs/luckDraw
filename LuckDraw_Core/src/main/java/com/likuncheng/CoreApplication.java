package com.likuncheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;



@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@EnableDistributedTransaction
@EnableHystrix
public class CoreApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

}
