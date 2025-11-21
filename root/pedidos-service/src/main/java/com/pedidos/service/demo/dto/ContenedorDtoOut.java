package com.pedidos.service.demo.dto;

import com.commonlib.Enums.EstadosContenedor;

public record ContenedorDtoOut(
        Long id,
        Double peso,
        Double volumen,
        EstadosContenedor estado,
        Long idUbicacionUltima) {

}
