package com.gateway.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class ResourceServerConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http.authorizeHttpRequests(auth -> 
        auth

        .requestMatchers(HttpMethod.GET,"/protected/solicitudes/**/cliente/**")
            .hasAnyRole("CLIENTE", "ADMIN")
        .requestMatchers(HttpMethod.GET,"/protected/solicitudes/cliente/**")
            .hasAnyRole("CLIENTE", "ADMIN")

        .requestMatchers(HttpMethod.GET,"/protected/solicitudes")
            .hasAnyRole( "ADMIN")
        .requestMatchers(HttpMethod.POST,"/protected/solicitudes")
            .hasAnyRole( "CLIENTE", "ADMIN")

        .requestMatchers(HttpMethod.GET,"/protected/tramos/camion/**")
            .hasAnyRole( "TRANSPORTISTA", "ADMIN")
        .requestMatchers(HttpMethod.PUT,"/protected/tramos/**")
            .hasAnyRole( "TRANSPORTISTA", "ADMIN")
        .requestMatchers(HttpMethod.GET,"/protected/tramos")
            .hasAnyRole( "ADMIN")

        .requestMatchers(HttpMethod.PUT, "/controlled/tramos/finalizar/**")
            .hasAnyRole( "TRANSPORTISTA", "ADMIN")
        .requestMatchers(HttpMethod.PUT, "/controlled/rutas/**")
            .hasAnyRole( "TRANSPORTISTA", "ADMIN")
            
        .requestMatchers("/protected/**")
            .hasRole("ADMIN")
        .requestMatchers("/controlled/**")
            .hasRole("ADMIN")

        .anyRequest()
        .authenticated()
        )
        .oauth2ResourceServer(o -> o.jwt().jwtAuthenticationConverter(new JwtConverter()));
        return http.build();

    }
}