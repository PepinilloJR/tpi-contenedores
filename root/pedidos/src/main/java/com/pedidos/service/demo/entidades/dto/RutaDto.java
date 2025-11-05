package com.pedidos.service.demo.entidades.dto;

import java.math.BigDecimal;

public record RutaDto(    
    Long id,
    Integer cantidadTramos,
    Integer cantidadDepositos,
    BigDecimal costoPorTramo
    ) {

}