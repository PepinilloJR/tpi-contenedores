package com.pedidos.service.demo.dto;

public record CamionDtoHttp(
        Long id,
        Long idTarifa,
        String patente,
        String nombreTransportista,
        String telefonoTransportista,
        Double capacidadPeso,
        Double capacidadVolumen,
        Double consumoCombustiblePromedio,
        Boolean disponible) {

}
