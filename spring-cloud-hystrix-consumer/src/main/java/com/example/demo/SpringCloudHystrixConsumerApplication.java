package com.example.demo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
@EnableCircuitBreaker
@EnableHystrixDashboard
public class SpringCloudHystrixConsumerApplication {
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/cloudRestInfo")
    @HystrixCommand(fallbackMethod = "staticContent", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500")
    })
    public String getCloudRestInfo(@RequestParam(required = false) Long sleepTime) throws InterruptedException {
        if (sleepTime != null) Thread.sleep(sleepTime);
        return this.restTemplate.getForObject("http://localhost:7000/hello", String.class);
    }

    public String staticContent(Long sleepTime) {
        return "server is down";
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudHystrixConsumerApplication.class, args);
    }

}
