package com.admin.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.admin.util.JwtUtil;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void apply(RequestTemplate template) {
        String token = jwtUtil.generateInternalToken();
        template.header("Authorization", "Bearer " + token);
    }
}
