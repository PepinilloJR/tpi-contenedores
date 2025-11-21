package com.pedidos.service.demo.dto;

public record  TramoTentativoDtoOut (
    String tipo,
    Double distancia,
    UbicacionTramoDtoOut origen,
    UbicacionTramoDtoOut destino
) {
    
}
