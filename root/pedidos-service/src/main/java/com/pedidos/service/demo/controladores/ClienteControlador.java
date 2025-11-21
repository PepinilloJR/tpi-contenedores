package com.pedidos.service.demo.controladores;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.commonlib.entidades.Cliente;
import com.pedidos.service.demo.servicios.ClienteServicio;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import com.pedidos.service.demo.dto.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteControlador {
    private final ClienteServicio servicio;

    public ClienteControlador(ClienteServicio servicio) {
        this.servicio = servicio;
    }

    @Operation(summary = "Crear una cliente", description = "Crea un nuevo Cliente")
    @PostMapping
    public ResponseEntity<ClienteDto> crear(@Valid @RequestBody ClienteDto clienteDto) {
        Cliente clienteEntidad = DtoHandler.convertirClienteEntidad(clienteDto);
        Cliente clienteCreado = servicio.crear(clienteEntidad);
        return ResponseEntity.status(201).body(DtoHandler.convertirClienteDto(clienteCreado));
    }

    @Operation(summary = "Actualizar un cliente", description = "Actualiza un Cliente dado segun id")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDto> actualizar(@PathVariable Long id, @RequestBody ClienteDto clienteDto) {
        // soporta la actualizacion parcial, aplicando campos no nulos del dto y guardo

        Cliente clienteActual = servicio.obtenerPorId(id);

        clienteActual.setNombre(clienteDto.nombre() != null ? clienteDto.nombre() : clienteActual.getNombre());
        clienteActual.setApellido(clienteDto.apellido() != null ? clienteDto.apellido() : clienteActual.getApellido());
        clienteActual.setTelefono(clienteDto.telefono() != null ? clienteDto.telefono() : clienteActual.getTelefono());
        clienteActual
                .setDireccion(clienteDto.direccion() != null ? clienteDto.direccion() : clienteActual.getDireccion());
        clienteActual.setDni(clienteDto.dni() != null ? clienteDto.dni() : clienteActual.getDni());

        Cliente clienteActualizado = servicio.actualizar(id, clienteActual);

        return ResponseEntity.ok(DtoHandler.convertirClienteDto(clienteActualizado));
    }

    @Operation(summary = "Obtener un Cliente", description = "Obtiene un Cliente dado segun id")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> obtener(@PathVariable Long id) {
        Cliente cliente = servicio.obtenerPorId(id);
        return ResponseEntity.ok(DtoHandler.convertirClienteDto(cliente));
    }

    @Operation(summary = "Obtener todos los Clientes", description = "Obtiene todos los Clientes registrados")
    @GetMapping
    public ResponseEntity<List<ClienteDto>> obtenerTodos() {
        List<Cliente> lista = servicio.listarTodos();
        List<ClienteDto> dtos = lista.stream().map(DtoHandler::convertirClienteDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Borrar un Clientes", description = "Borra un Cliente dado segun id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
