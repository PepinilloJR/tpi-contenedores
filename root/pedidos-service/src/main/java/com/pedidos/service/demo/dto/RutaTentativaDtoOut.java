package com.pedidos.service.demo.dto;

public record  RutaTentativaDtoOut (
    Long idRuta,
    Double distanciaTotal,
    Double tiempoEstimado,
    Integer cantidadTramos,
    Integer cantidadDepositos
    
) {

}
