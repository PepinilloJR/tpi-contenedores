package com.gateway.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestClient;

public class pedidosController {
    @Autowired
    RestClient pedidosClient;

    @Autowired
    RestClient camionesClient;
}
