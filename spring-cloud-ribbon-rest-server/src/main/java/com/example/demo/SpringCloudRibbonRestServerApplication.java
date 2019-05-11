package com.example.demo;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AvailabilityFilteringRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
@RibbonClient(name = "spring-cloud-rest", configuration = RibbonConfigs.class)
public class SpringCloudRibbonRestServerApplication {
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/port")
    public String getPort() {
        return this.restTemplate.getForObject("http://spring-cloud-rest/port", String.class);
    }

    @GetMapping("/")
    public String healthStatus() {
        return "good";
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudRibbonRestServerApplication.class, args);
    }

}

class RibbonConfigs {
    /*@Autowired
    public IClientConfig ribbonClientConfig;*/

    @Bean
    public IPing ping(IClientConfig iClientConfig) {
        return new PingUrl();
    }

    @Bean
    public IRule rule(IClientConfig iClientConfig) {
        return new AvailabilityFilteringRule();
    }
}