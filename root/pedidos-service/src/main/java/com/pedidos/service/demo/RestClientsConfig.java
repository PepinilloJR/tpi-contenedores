package com.pedidos.service.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientsConfig {

    @Bean
    RestClient distanciaClient() {
        return RestClient.create("http://osrm:5000/route/v1/");
    }

}
