package com.gateway.demo.controllers;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/protected/test-uri")
public class TestController {
    
    @Autowired
    RestClient pedidosClient;

    @GetMapping
    public Map<String, Object> getString(@AuthenticationPrincipal Jwt jwt) {
        // puedo obtener datos del jwt, podria en este caso obtener el nombre o datos para asociar un cliente o camionero que esta 
        // en sesion y buscar lo que le corresponde a el
        
        return jwt.getClaims();

    } 

}
