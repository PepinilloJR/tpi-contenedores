package com.gateway.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/protected/test-uri")
public class TestController {
    
    @GetMapping
    public String getString() {
        return "hola";
    } 

}
