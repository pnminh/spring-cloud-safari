package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.security.Principal;

@SpringBootApplication
@RestController
@EnableOAuth2Client
public class SpringCloudAuthClientApplication {
    @Bean
    public PasswordEncoder getPasswordEncooder(){
        return NoOpPasswordEncoder.getInstance();
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, UserDetailsService userDetailsService) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(getPasswordEncooder()).and().eraseCredentials(false);
        /* configure user-service, password-encoder etc ... */
    }
    @Autowired
    @Qualifier("oAuth2RestTemplate")
    private OAuth2RestTemplate oAuth2RestTemplate;

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @Bean("restTemplate")
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @GetMapping("/protectedResource")
    public String protectedResource(Principal principal) throws URISyntaxException {
        User user = (User) ((Authentication) principal).getPrincipal();
        //add user/pass to accesstoken request for password grant
        AccessTokenRequest accessTokenRequest = this.oAuth2RestTemplate.getOAuth2ClientContext().getAccessTokenRequest();
        accessTokenRequest.set("username", user.getUsername());
        accessTokenRequest.set("password", user.getPassword());
        return oAuth2RestTemplate.getForObject("http://localhost:3030/admin", String.class);
    }

    @Bean("oAuth2RestTemplate")
    public OAuth2RestTemplate getOAuth2RestTemplate(OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, new DefaultOAuth2ClientContext());
    }

    @Bean
    public OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails() {
        ResourceOwnerPasswordResourceDetails details = new ResourceOwnerPasswordResourceDetails();
        details.setClientId("webAppClient");
        details.setClientSecret("webAppPassword");
        details.setGrantType("password");
        details.setAccessTokenUri("http://localhost:3030/oauth/token");
        /*details.setUsername("admin");
        details.setPassword("password1");*/

        return details;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudAuthClientApplication.class, args);
    }

}
