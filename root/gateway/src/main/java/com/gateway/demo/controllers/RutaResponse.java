package com.gateway.demo.controllers;

import java.util.List;

import lombok.Data;

@Data
public class RutaResponse {
    private List<Route> routes;
}
