package com.example.keycloakdemo;

import com.example.keycloakdemo.Configs.PathBasedKeycloakConfigResolver;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KeycloakDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeycloakDemoApplication.class, args);
	}

	@Bean
	@ConditionalOnMissingBean(PathBasedKeycloakConfigResolver.class)
	public KeycloakConfigResolver keycloakConfigResolver() {
		return new PathBasedKeycloakConfigResolver();
	}

}
