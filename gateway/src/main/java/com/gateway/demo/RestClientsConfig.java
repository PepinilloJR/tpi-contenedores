package com.gateway.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientsConfig {

    @Bean
    RestClient pedidosClient() {
        return RestClient.create("http://localhost:8001/api/solicitudes");
    }

    @Bean
    RestClient camionesClient() {
        return RestClient.create("http://localhost:8001/api/camiones");
    }
}
