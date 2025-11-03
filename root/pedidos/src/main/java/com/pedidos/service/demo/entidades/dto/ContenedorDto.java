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

public class ContenedorDto {
    private Long id;
    private Double peso;
    private Double volumen;
    private String estado;
    private ClienteDto clienteDto;

}
