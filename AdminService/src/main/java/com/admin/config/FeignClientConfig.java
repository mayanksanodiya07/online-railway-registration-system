package com.admin.config;

import com.admin.exception.TrainNotFoundException;
import com.admin.exception.TrainServiceException;

import feign.Client;
import feign.Response;
import feign.codec.ErrorDecoder;
import feign.httpclient.ApacheHttpClient;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

	  @Bean
    public Client feignClient() {
        CloseableHttpClient httpClient = HttpClients.custom().build();
        return new ApacheHttpClient(httpClient); // ✅ Use ApacheHttpClient wrapper
    }
	  
    @Bean
    public ErrorDecoder errorDecoder() {
        return new AdminFeignErrorDecoder();
    }

    private static class AdminFeignErrorDecoder implements ErrorDecoder {

        private final ErrorDecoder defaultDecoder = new Default();
 
        @Override
        public Exception decode(String methodKey, Response response) {
            int status = response.status();
            System.out.println(status);
            if (status == 404) {
                return new TrainNotFoundException("Requested resource not found (404).");
            } else if (status >= 400 ) {
                return new TrainServiceException("Server error from TrainService: " + status);
            }

            return defaultDecoder.decode(methodKey, response); 
        }
    }
}
