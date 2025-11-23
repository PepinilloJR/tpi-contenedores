package com.pedidos.service.demo.dto;

import com.commonlib.Enums.EstadoSolicitud;

public record SolicitudDtoOut(
        Long id,
        EstadoSolicitud estado,
        Double costoEstimado,
        Double costoFinal,
        Long idCliente,
        Long idContedor,
        Long idOrigen,
        Long idDestino
    ) {

}
