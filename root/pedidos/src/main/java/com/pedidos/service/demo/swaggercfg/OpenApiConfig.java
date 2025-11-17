package com.pedidos.service.demo.swaggercfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pedidos/Solicitud API")
                        .description(
                                "Documentación de la API de Solicitudes, Clientes, Contenedores, Rutas, Seguimiento, Tramos y Ubicaciones")
                        .version("1.0"));
    }
}
