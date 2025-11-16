package com.gateway.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientsConfig {

    @Bean
    RestClient pedidosClient() {
        return RestClient.create("http://pedidos:8001/api/solicitudes");
    }
    
    @Bean
    RestClient rutasClient() {
        return RestClient.create("http://pedidos:8001/api/rutas");
    }

    @Bean
    RestClient tramosClient() {
        return RestClient.create("http://pedidos:8001/api/tramos");
    }

    @Bean
    RestClient clientesClient() {
        return RestClient.create("http://pedidos:8001/api/clientes");
    }

    @Bean
    RestClient contenedoresClient() {
        return RestClient.create("http://pedidos:8001/api/contenedores");
    }

    @Bean
    RestClient depositosClient() {
        return RestClient.create("http://pedidos:8001/api/ubicaciones");
    }

    @Bean
    RestClient distanciaClient() {
        return RestClient.create("http://osrm:5000/route/v1/");
    }

    // Es otro el puerto de camiones

    @Bean
    RestClient camionesClient() {
        return RestClient.create("http://pedidos:8001/api/camiones");
    }
}
