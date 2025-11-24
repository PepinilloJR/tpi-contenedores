package com.tpi.depositosservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientsConfig {

    @Bean
    RestClient solicitudClient() {
        System.out.println("CONEXION ====================");
        return RestClient.create("http://pedidos:8001");
    }

}
