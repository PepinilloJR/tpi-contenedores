package com.pedidos.service.demo.dto;

import com.commonlib.Enums.EstadoSolicitud;

public record SolicitudDtoIn(
        EstadoSolicitud estado,
        Double costoFinal) {

}
