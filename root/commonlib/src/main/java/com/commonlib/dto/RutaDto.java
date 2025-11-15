package com.commonlib.dto;

import java.math.BigDecimal;

public record RutaDto(    
    Long id,
    Integer cantidadTramos,
    Integer cantidadDepositos,
    BigDecimal costoPorTramo
    ) {
}