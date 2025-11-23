package com.tpi.depositosservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientsConfig {

    @Bean
    RestClient solicitudClient() {
        return RestClient.create("http://solicitudes:8001");
    }

}
