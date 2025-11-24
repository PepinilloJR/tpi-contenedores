package com.pedidos.service.demo.dto;

public record RutaDtoIn (
    Integer cantidadTramos,
    Integer cantidadDepositos,
    Double tiempoReal,
    Long solicitudId
) {}
