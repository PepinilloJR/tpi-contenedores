package com.pedidos.service.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientsConfig {

    @Bean
    RestClient solicitudesClient() {
        return RestClient.create("http://solicitudes:8001");
    }

    @Bean
    RestClient tarifasClient() {
        return RestClient.create("http://camiones:8002/api/tarifas");
    }

}
