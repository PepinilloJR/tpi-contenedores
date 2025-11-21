package com.pedidos.service.demo.dto;

public record UbicacionDtoOut(
    Long id,
    Double latitud,
    Double longitud,
    String tipo,
    String nombre,
    Double costo
) 
{}
