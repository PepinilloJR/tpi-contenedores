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

import com.commonlib.ClienteDto;
import com.pedidos.service.demo.entidades.Cliente;
import com.pedidos.service.demo.servicios.ClienteServicio;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteControlador {
    private final ClienteServicio servicio;

    public ClienteControlador(ClienteServicio servicio) {
        this.servicio = servicio;
    }

    // helper privado entidad -> dto
    private ClienteDto convertirDto(Cliente c) {
        if (c == null)
            return null;
        return new ClienteDto(c.getId(), c.getNombre(), c.getApellido(), c.getTelefono(), c.getDireccion(), c.getDni());
    }

    // helper privado dto -> entidad (para crear)
    private Cliente convertirEntidad(ClienteDto dto) {
        Cliente c = new Cliente();
        c.setNombre(dto.nombre());
        c.setApellido(dto.apellido());
        c.setTelefono(dto.telefono());
        c.setDireccion(dto.direccion());
        c.setDni(dto.dni());
        return c;
    }

    @PostMapping
    public ResponseEntity<ClienteDto> crear(@Valid @RequestBody ClienteDto clienteDto) {
        Cliente ClienteEntidad = convertirEntidad(clienteDto);
        Cliente ClienteCreado = servicio.crear(ClienteEntidad);
        return ResponseEntity.status(201).body(convertirDto(ClienteCreado));
    }
    

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

        return ResponseEntity.ok(convertirDto(clienteActualizado));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> obtener(@PathVariable Long id) {
        Cliente cliente = servicio.obtenerPorId(id);
        return ResponseEntity.ok(convertirDto(cliente));
    }


    @GetMapping
    public ResponseEntity<String> obtenerTodos() {
        List<Cliente> lista = servicio.listarTodos();
        List<ClienteDto> dtos = lista.stream().map(this::convertirDto).collect(Collectors.toList());
        return ResponseEntity.ok("server loco");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
