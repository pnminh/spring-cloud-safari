package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@SpringBootApplication
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RestController
public class SpringCloudResourceServerApplication {
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin(Principal principal) {
        return "Hello, "+ principal.getName()+ ". Only admin can see";
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudResourceServerApplication.class, args);
    }

}
