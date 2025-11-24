package com.pedidos.service.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientsConfig {

    @Bean
    RestClient solicitudesClient() {
        return RestClient.create("http://pedidos:8001");
    }

    @Bean
    RestClient camionesClient() {
        return RestClient.create("http://camiones:8002/api/camiones");
    } 

    @Bean
    RestClient tarifasClient() {
        return RestClient.create("http://camiones:8002/api/tarifas");
    }

    @Bean
    RestClient estadiasClient() {
        return RestClient.create("http://depositos:8003/api/estadias");
    }

    @Bean
    RestClient distanciaClient() {
        return RestClient.create("http://osrm:5000/route/v1/");
    }

}
