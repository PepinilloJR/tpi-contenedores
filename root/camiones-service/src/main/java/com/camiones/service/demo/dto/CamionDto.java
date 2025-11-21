package com.camiones.service.demo.dto;

public record CamionDto(
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
