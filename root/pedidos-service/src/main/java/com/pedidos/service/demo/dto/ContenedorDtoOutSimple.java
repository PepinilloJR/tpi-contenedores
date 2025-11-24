package com.pedidos.service.demo.dto;

import com.commonlib.Enums.EstadosContenedor;

public record ContenedorDtoOutSimple(
        Long id,
        EstadosContenedor estado,
        Long idUbicacionUltima) {

}
