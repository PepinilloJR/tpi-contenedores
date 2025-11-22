package com.pedidos.service.demo.dto;

import com.commonlib.Enums.EstadosTramo;

public record  TramoTentativoDtoOut (
    String tipo,
    Double distancia,
    UbicacionTramoDtoOut origen,
    EstadosTramo estado,
    UbicacionTramoDtoOut destino
) {
    
}
