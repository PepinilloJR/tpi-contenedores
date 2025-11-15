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

        .requestMatchers(HttpMethod.GET,"/protected/solicitudes")
            .hasAnyRole("CLIENTE", "ADMIN")
        .requestMatchers(HttpMethod.POST,"/protected/solicitudes")
            .hasAnyRole( "CLIENTE", "ADMIN")

        .requestMatchers(HttpMethod.PUT,"/protected/tramos")
            .hasRole( "TRANSPORTISTA")
        .requestMatchers(HttpMethod.GET,"/protected/tramos")
            .hasRole( "TRANSPORTISTA")

        .requestMatchers("/protected/**")
            .hasRole("ADMIN")


        .anyRequest()
        .authenticated()
        )
        .oauth2ResourceServer(o -> o.jwt().jwtAuthenticationConverter(new JwtConverter()));
        return http.build();

    }
}
