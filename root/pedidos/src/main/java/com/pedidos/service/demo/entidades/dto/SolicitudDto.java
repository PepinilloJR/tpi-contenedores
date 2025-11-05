package com.pedidos.service.demo.entidades.dto;

public record SolicitudDto(
        Long id,
        Double costoEstimado,
        Double tiempoEstimado,
        Double tiempoReal,
        Double costoFinal
        ) {

}