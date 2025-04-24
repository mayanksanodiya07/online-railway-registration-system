package com.gateway.filter;
 
import java.util.List; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest; 
import org.springframework.stereotype.Component; 
import org.springframework.web.server.ServerWebExchange;

import com.gateway.util.JwtUtil;
 
import reactor.core.publisher.Mono;
@Component
public class GatewayJwtFilter implements GlobalFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest req = exchange.getRequest();
        String path = req.getURI().getPath();

        if (path.startsWith("/auth") || path.startsWith("/train")) return chain.filter(exchange); // Skip for auth

        List<String> authHeaders = req.getHeaders().getOrEmpty("Authorization");

        if (authHeaders.isEmpty() || !authHeaders.get(0).startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeaders.get(0).substring(7);
        if (!jwtUtil.isTokenValid(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange); // Pass to microservices
    }
}
