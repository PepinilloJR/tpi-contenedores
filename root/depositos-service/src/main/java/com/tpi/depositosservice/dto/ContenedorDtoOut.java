package com.tpi.depositosservice.dto;

import com.commonlib.Enums.EstadosContenedor;

public record ContenedorDtoOut(
        Long id,
        Double peso,
        Double volumen,
        EstadosContenedor estado,
        Long idUbicacionUltima) {

}
