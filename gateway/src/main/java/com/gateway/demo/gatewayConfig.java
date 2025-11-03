package com.gateway.demo;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class gatewayConfig {

    @Bean
    public RouteLocator configRouteLocator(RouteLocatorBuilder builder) {


        return builder.routes()
            .route(p -> p.path("/pedidos/get").uri("http://localhost:8001"))
            .route(p -> p.path("/camiones/get").uri("http://localhost:8002"))
            .route(p -> p.path("/depositos/get").uri("http://localhost:8003"))
            
            .build();
    }

}
