package com.pedidos.service.demo.dto;

public record UbicacionDtoIn(
    Double latitud,
    Double longitud,
    String tipo,
    String nombre,
    Double costo
) {}