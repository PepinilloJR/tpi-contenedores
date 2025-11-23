package com.tpi.depositosservice.dto;

import com.commonlib.Enums.EstadoSolicitud;

public record SolicitudDtoOut(
        Long id,
        EstadoSolicitud estado,
        Double costoEstimado,
        Double costoFinal,
        Long idCliente,
        Long idContenedor,
        Long idOrigen,
        Long idDestino
    ) {

}
