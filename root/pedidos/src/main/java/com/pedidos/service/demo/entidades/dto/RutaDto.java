package com.pedidos.service.demo.entidades.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RutaDto {
    private Long id;
    private Integer cantidadTramos;
    private Integer cantidadDepositos;
    private BigDecimal costoPorTramo;
}