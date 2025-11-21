package com.pedidos.service.demo.servicios;

import java.util.List;

import org.springframework.stereotype.Service;

import com.commonlib.entidades.Cliente;
import com.pedidos.service.demo.exepciones.ResourceNotFoundException;
import com.pedidos.service.demo.repositorios.ClienteRepositorio;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteServicio {
    private final ClienteRepositorio repositorio;

    @Transactional
    public Cliente crear(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Error al crear, cliente invalido, no puede ser nulo");
        }

        validarDatos(cliente);

        // Dni unico
        if (cliente.getDni() != null && repositorio.existsByDni(cliente.getDni())) {
            throw new IllegalArgumentException("Error al crear, ya existe un cliente con dni " + cliente.getDni());
        }

        return repositorio.save(cliente);
    }

    @Transactional
    public Cliente crearSiNoExiste(Cliente cliente) {

        return repositorio.findByDni(cliente.getDni())
                .orElseGet(() -> repositorio.save(cliente));
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Error al obtener, Cliente no encontrado con id " + id));
    }

    // Maybe al pedo?

    @Transactional(readOnly = true)
    public List<Cliente> obtenerPorNombre(String nombre) {
        return repositorio.findByNombre(nombre);
    }

    @Transactional
    public Cliente actualizar(Long id, Cliente datos) {
        Cliente existente = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Error al actualizar, cliente no encontrado con id " + id));

        if (datos == null) {
            throw new IllegalArgumentException("Error al actualizar, datos de actualizacion invalido");
        }

        if (datos.getDni() != null && !datos.getDni().equals(existente.getDni()) &&
                repositorio.existsByDni(datos.getDni())) {
            throw new IllegalArgumentException("Error al actualizar, ya existe un cliente con dni " + datos.getDni());

        }

        existente.setNombre(datos.getNombre() != null ? datos.getNombre() : existente.getNombre());
        existente.setApellido(datos.getApellido() != null ? datos.getApellido() : existente.getApellido());
        existente.setTelefono(datos.getTelefono() != null ? datos.getTelefono() : existente.getTelefono());
        existente.setDireccion(datos.getDireccion() != null ? datos.getDireccion() : existente.getDireccion());
        existente.setDni(datos.getDni() != null ? datos.getDni() : existente.getDni());

        validarDatos(existente);

        return repositorio.save(existente);

    }

    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ResourceNotFoundException("Error al eliminar, cliente no encontrado con id " + id);
        }
        repositorio.deleteById(id);
    }

    private void validarDatos(Cliente c) {
        if (c.getNombre() == null || c.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }
        if (c.getApellido() == null || c.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido del cliente es obligatorio");
        }
        if (c.getTelefono() == null || c.getTelefono().isBlank()) {
            throw new IllegalArgumentException("El teléfono del cliente es obligatorio");
        }
        if (c.getDireccion() == null || c.getDireccion().isBlank()) {
            throw new IllegalArgumentException("La dirección del cliente es obligatoria");
        }
        if (c.getDni() == null) {
            throw new IllegalArgumentException("El DNI del cliente es obligatorio");
        }
    }

}
