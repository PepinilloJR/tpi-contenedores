package com.pedidos.service.demo.dto;

public record RutaDtoIn (
    Integer cantidadTramos,
    Integer cantidadDepositos,
    Double costoPorTramo,
    Double tiempoReal,
    Long solicitudId
) {}
