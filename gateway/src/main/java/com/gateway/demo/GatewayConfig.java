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
    public RouterFunction<ServerResponse> getRoute() {
        return route("pedidos").path("/protected/clientes", p -> p.route(RequestPredicates.all(), HandlerFunctions.http()))
                .before(BeforeFilterFunctions.rewritePath("/protected/clientes", "/api/clientes"))
                .before(BeforeFilterFunctions.uri("http://localhost:8001/api/clientes"))

                .path("/protected/contenedores", p -> p.route(RequestPredicates.all(), HandlerFunctions.http()))
                .before(BeforeFilterFunctions.rewritePath("/protected/contenedores", "/api/contenedores"))
                .before(BeforeFilterFunctions.uri("http://localhost:8001/api/contenedores"))

                .path("/protected/tramos", p -> p.route(RequestPredicates.all(), HandlerFunctions.http()))
                .before(BeforeFilterFunctions.rewritePath("/protected/tramos", "/api/tramos"))
                .before(BeforeFilterFunctions.uri("http://localhost:8001/api/tramos"))
                
                .path("/protected/camiones", p -> p.route(RequestPredicates.all(), HandlerFunctions.http()))
                .before(BeforeFilterFunctions.rewritePath("/protected/camiones", "/api/camiones"))
                .before(BeforeFilterFunctions.uri("http://localhost:8002/api/camiones"))
            
                .path("/protected/rutas", p -> p.route(RequestPredicates.all(), HandlerFunctions.http()))
                .before(BeforeFilterFunctions.rewritePath("/protected/rutas", "/api/rutas"))
                .before(BeforeFilterFunctions.uri("http://localhost:8001/api/rutas"))
                            
                .path("/protected/solicitudes", p -> p.route(RequestPredicates.all(), HandlerFunctions.http()))
                .before(BeforeFilterFunctions.rewritePath("/protected/solicitudes", "/api/solicitudes"))
                .before(BeforeFilterFunctions.uri("http://localhost:8001/api/solicitudes"))
            
                .path("/protected/ubicaciones", p -> p.route(RequestPredicates.all(), HandlerFunctions.http()))
                .before(BeforeFilterFunctions.rewritePath("/protected/ubicaciones", "/api/ubicaciones"))
                .before(BeforeFilterFunctions.uri("http://localhost:8001/api/ubicaciones"))
            

            .build();
    }
}