package com.pedidos.service.demo.dto;

public record SolicitudDtoOut(
        Long id,
        String estado,
        Double costoEstimado,
        Double costoFinal
    ) {

}
