package com.pedidos.service.demo.dto;

public record SolicitudDtoCreacion(
        Long id,
        Long idCliente, // puede ser nulo, si es nulo y me vienen datos tengo que crear el cliente y
                        // asegurar que no tenga mismo dni
        String nombreCliente, // ya asegurado se supone
        String apellidoCliente, // ya asegurado se supone
        String telefonoCliente, // ya asegurado se supone
        String direccionCliente, // ya asegurado se supone
        Integer dniCliente, // ya asegurado se supone
        Double peso, // ya asegurado se supone
        Double volumen, // ya asegurado se supone
        Double latitudOrigen,
        Double longitudOrigen,
        Double latitudDestino,
        Double longitudDestino) {

}
