package com.commonlib.dto;

import java.math.BigDecimal;

public record RutaDto(    
    Long id,
    SolicitudDto solicitudDto,
    Integer cantidadTramos,
    Integer cantidadDepositos,
    BigDecimal costoPorTramo,
    Long[] depositosID,
    Double distanciaTotal 
    ) {
}