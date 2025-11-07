package com.pedidos.service.demo.servicios;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pedidos.service.demo.entidades.Cliente;
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
        return repositorio.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente obtenerPorId(Long id) {
        return repositorio.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + id));
    }

    // Maybe al pedo?

    @Transactional(readOnly = true)
    public List<Cliente> obtenerPorNombre(String nombre) {
        return repositorio.findByNombre(nombre);
    }

    @Transactional
    public Cliente actualizar(Long id, Cliente clienteActualizado) {
        Cliente existente = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + id));

        existente.setNombre(clienteActualizado.getNombre());
        existente.setApellido(clienteActualizado.getApellido());
        existente.setTelefono(clienteActualizado.getTelefono());
        existente.setDireccion(clienteActualizado.getDireccion());
        existente.setDni(clienteActualizado.getDni());

        return repositorio.save(existente);

    }

    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ResourceNotFoundException("Cliente no encontrado con id " + id);
        }
        repositorio.deleteById(id);
    }

}
