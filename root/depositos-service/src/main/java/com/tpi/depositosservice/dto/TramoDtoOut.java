package com.tpi.depositosservice.dto;

import java.time.LocalDateTime;

import com.commonlib.Enums.EstadosTramo;
import com.commonlib.Enums.TiposTramos;

public record TramoDtoOut (
    Long id,
    Double distancia,
    LocalDateTime fechaInicio,
    LocalDateTime fechaFin,
    TiposTramos tipo,
    EstadosTramo estado,
    Integer combustibleConsumido,
    Long idCamion,
    Long idRuta,
    Long idOrigen,
    Long idDestino,
    Double costoAproximado,
    Double costoReal,
    Double costoVolumen,
    Double costoKilometro

) 
{

}