package com.pedidos.service.demo.dto;

import java.time.LocalDateTime;

import com.commonlib.Enums.EstadoSolicitud;

public record SeguimientoDtoOut(
        EstadoSolicitud estado,
        LocalDateTime fecha,
        String comentario) {

}
