package com.pedidos.service.demo.dto;

public record RutaTentativaDtoIn(    
    Long pedidoId,
    Long[] depositosId
) {
}
