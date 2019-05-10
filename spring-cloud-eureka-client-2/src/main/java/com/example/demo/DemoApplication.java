package com.example.demo;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class DemoApplication {
	@Autowired
	private EurekaClient eurekaClient;
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	@GetMapping("/clientInfo")
	public String getClientInfo(){
		InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("eureka-client-2",false);
		return instanceInfo.getHomePageUrl();
	}
}
