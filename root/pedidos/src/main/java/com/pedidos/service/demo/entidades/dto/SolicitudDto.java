package com.pedidos.service.demo.entidades.dto;
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
public class SolicitudDto {
    private Long id;
    private Double costoEstimado;
    private Double tiempoEstimado;
    private Double tiempoReal;
    private Double costoFinal;
}