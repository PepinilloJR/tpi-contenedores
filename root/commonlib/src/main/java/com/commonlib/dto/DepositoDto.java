package com.commonlib.dto;


public record DepositoDto(
    Long idDeposito,
    String nombre,
    String direccion,
    Double latitud,
    Double longitud,
    Double costoEstadia
) {
}