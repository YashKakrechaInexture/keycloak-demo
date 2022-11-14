package com.example.keycloakdemo.Configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CustomConfigs {

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
