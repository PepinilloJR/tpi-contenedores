package com.gateway.demo;

import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class GatewayConfig {

    @Bean
    public RouterFunction<ServerResponse> clientesRouterFunction() {
        return route("pedidos").path("/protected/clientes", p -> p.route(RequestPredicates.all(), HandlerFunctions.http()))
                .before(BeforeFilterFunctions.rewritePath("/protected/clientes", "/api/clientes"))
                .before(BeforeFilterFunctions.uri("http://pedidos:8001/api/clientes"))

            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> tramosRouterFunction() {
        return route("tramos").path("/protected/tramos", p -> p.route(RequestPredicates.all(), HandlerFunctions.http()))
                .before(BeforeFilterFunctions.rewritePath("/protected/tramos", "/api/tramos"))
                .before(BeforeFilterFunctions.uri("http://pedidos:8001/api/tramos"))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> contenedoresRouterFunction() {
        return route("contenedores").path("/protected/contenedores", p -> p.route(RequestPredicates.all(), HandlerFunctions.http()))
                .before(BeforeFilterFunctions.rewritePath("/protected/contenedores", "/api/contenedores"))
                .before(BeforeFilterFunctions.uri("http://pedidos:8001/api/contenedores"))

            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> camionesRouterFunction() {
        return route("camiones").path("/protected/camiones", p -> p.route(RequestPredicates.all(), HandlerFunctions.http()))
                .before(BeforeFilterFunctions.rewritePath("/protected/camiones", "/api/camiones"))
                .before(BeforeFilterFunctions.uri("http://camiones:8002/api/camiones"))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> rutasRouterFunction() {
        return route("rutas").path("/protected/rutas", p -> p.route(RequestPredicates.all(), HandlerFunctions.http()))
                .before(BeforeFilterFunctions.rewritePath("/protected/rutas", "/api/rutas"))
                .before(BeforeFilterFunctions.uri("http://pedidos:8001/api/rutas"))
            .build();
    }

        @Bean
    public RouterFunction<ServerResponse> solicitudesRouterFunction() {
        return route("solicitudes").path("/protected/solicitudes", p -> p.route(RequestPredicates.all(), HandlerFunctions.http()))
                .before(BeforeFilterFunctions.rewritePath("/protected/solicitudes", "/api/solicitudes"))
                .before(BeforeFilterFunctions.uri("http://pedidos:8001/api/solicitudes"))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> ubicacionesRouterFunction() {
        return route("ubicaciones").path("/protected/ubicaciones", p -> p.route(RequestPredicates.all(), HandlerFunctions.http()))
                .before(BeforeFilterFunctions.rewritePath("/protected/ubicaciones", "/api/ubicaciones"))
                .before(BeforeFilterFunctions.uri("http://pedidos:8001/api/ubicaciones")).build();
    }

    @Bean
    public RouterFunction<ServerResponse> depositosRouterFunction() {
        return route("estadias").path("/protected/estadias", p -> p.route(RequestPredicates.all(), HandlerFunctions.http()))
                .before(BeforeFilterFunctions.rewritePath("/protected/estadias", "/api/estadias"))
                .before(BeforeFilterFunctions.uri("http://depositos:8003/api/estadias")).build();
    }
}

/* 
 
 */