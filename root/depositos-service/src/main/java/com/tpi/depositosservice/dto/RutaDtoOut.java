package com.tpi.depositosservice.dto;

public record RutaDtoOut(
        Long idRuta,
        Double distanciaTotal,
        Long solicitudId,
        Long tiempoEstimado, // en dias
        Long tiempoReal,
        Double costoPorTramo,
        Integer cantidadDepositos,
        Integer cantidadTramos) {

}
