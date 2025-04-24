package com.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
    	 http.csrf(csrf -> csrf.disable())
            .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll());
  	   
        return http.build();
    }
}
