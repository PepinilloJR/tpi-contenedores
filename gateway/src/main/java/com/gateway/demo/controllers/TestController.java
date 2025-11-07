package com.gateway.demo.controllers;


import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/protected/test-uri")
public class TestController {
    
    @GetMapping
    public Map<String, Object> getString(@AuthenticationPrincipal Jwt jwt) {
        return jwt.getClaims();

    } 

}
