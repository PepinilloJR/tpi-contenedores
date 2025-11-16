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
    RestClient clientesClient() {
        return RestClient.create("http://localhost:8001/api/clientes");
    }

    @Bean
    RestClient contenedoresClient() {
        return RestClient.create("http://localhost:8001/api/contenedores");
    }

    @Bean
    RestClient depositosClient() {
        return RestClient.create("http://localhost:8001/api/depositos");
    }

    @Bean
    RestClient distanciaClient() {
        return RestClient.create("http://localhost:5000/api/depositos");
    }

    // Es otro el puerto de camiones

    @Bean
    RestClient camionesClient() {
        return RestClient.create("http://pedidos:8001/api/camiones");
    }
}
