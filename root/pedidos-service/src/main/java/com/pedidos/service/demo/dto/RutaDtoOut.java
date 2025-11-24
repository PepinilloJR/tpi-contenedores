package com.pedidos.service.demo.dto;

public record RutaDtoOut(
    Long idRuta,
    Double distanciaTotal,
    Long solicitudId,
    Long tiempoEstimado, // en dias
    Long tiempoReal,
    Integer cantidadDepositos,
    Integer cantidadTramos
) {
    
}
