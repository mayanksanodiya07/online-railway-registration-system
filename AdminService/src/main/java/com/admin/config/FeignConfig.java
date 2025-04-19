package com.admin.config;

import feign.Client;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients; 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration; 

import feign.httpclient.ApacheHttpClient;

@Configuration
public class FeignConfig {

    @Bean
    public Client feignClient() {
        CloseableHttpClient httpClient = HttpClients.custom().build();
        return new ApacheHttpClient(httpClient); // ✅ Use ApacheHttpClient wrapper
    }
}
