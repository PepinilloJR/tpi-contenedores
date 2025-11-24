package com.pedidos.service.demo.servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commonlib.Enums.EstadoSolicitud;
import com.commonlib.Enums.EstadosContenedor;
import com.commonlib.Enums.TiposUbicacion;
import com.commonlib.entidades.Cliente;
import com.commonlib.entidades.Seguimiento;
import com.commonlib.entidades.Solicitud;
import com.commonlib.entidades.Ubicacion;
import com.pedidos.service.demo.dto.ContenedorDtoIn;
import com.pedidos.service.demo.dto.SolicitudDtoCreacion;
import com.pedidos.service.demo.dto.SolicitudDtoIn;
import com.pedidos.service.demo.exepciones.ResourceNotFoundException;
import com.pedidos.service.demo.repositorios.SolicitudRepositorio;

import lombok.RequiredArgsConstructor;

// costoEstimado, costoFinal puede ser null pero mayores a <=0 LITO
// el estado debe ser enum valido LITO, puede ser nulo solo al crear
// setear en estado en borrador al principio
// manejar el estado, cuando se actualiza guardar el estado anterior
// cuando el estado pasa a finalizado deberia guardar el anterior y actual
// No puede ser nulos:
// Cliente -> si no existe hay que crearlo
// Contenedor -> hay que crearlo
// No puede ser nula la ubicacion de origen y destino
// el seguimiento tampoco pero eso se maneja de otra forma

@Service
@RequiredArgsConstructor
public class SolicitudServicio {
    private final SolicitudRepositorio repositorio;
    private final ClienteServicio clienteServicio;
    private final ContenedorServicio contenedorServicio;
    private final UbicacionServicio ubicacionServicio;
    //private final TramoServicio tramoServicio;
    // private final RutaServicio rutaServicio;

    @Transactional
    public Solicitud crear(SolicitudDtoCreacion solicitud) {
        if (solicitud == null) {
            throw new IllegalArgumentException("Error al crear. Solicitud invalida, no puede ser nula");
        }

        try {
            var cliente = new Cliente(null, solicitud.nombreCliente(), solicitud.apellidoCliente(),
                    solicitud.telefonoCliente(), solicitud.direccionCliente(), solicitud.dniCliente());

            cliente = clienteServicio.crearSiNoExiste(cliente);

            // Origen
            TiposUbicacion tipoOrigen = TiposUbicacion.valueOf(solicitud.tipoOrigen().toUpperCase());
            TiposUbicacion tipoDestino = TiposUbicacion.valueOf(solicitud.tipoDestino().toUpperCase());
            var origen = new Ubicacion(null, solicitud.nombreOrigen(), tipoOrigen, solicitud.latitudOrigen(),
                    solicitud.longitudOrigen(), null);
            var destino = new Ubicacion(null, solicitud.nombreDestino(), tipoDestino, solicitud.latitudDestino(),
                    solicitud.longitudDestino(), null);
            origen = ubicacionServicio.crearSiNoExiste(origen);
            destino = ubicacionServicio.crearSiNoExiste(destino);

            // Contenedor
            var contenedorDto = new ContenedorDtoIn(null, solicitud.peso(), solicitud.volumen(),
                    EstadosContenedor.EN_PREPARACION, origen.getId());
            var contenedor = contenedorServicio.crear(contenedorDto);

            // Se puede manejar q en el dto me llegen los id de las ubicaciones

            var solicitudNueva = new Solicitud(null, EstadoSolicitud.BORRADOR, null, null, cliente, contenedor, origen,
                    destino,
                    new ArrayList<>());

            Seguimiento seguimientoInicial = new Seguimiento();
            seguimientoInicial.setEstadoAnterior(EstadoSolicitud.BORRADOR);
            seguimientoInicial.setFecha(LocalDateTime.now());
            seguimientoInicial.setComentario("La solicitud se encuentra en estado de BORRADOR.");
            solicitudNueva.getSeguimiento().add(seguimientoInicial);

            return repositorio.save(solicitudNueva);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error al crear la solicitud: " + e.getMessage(), e);
        }

    }

    @Transactional(readOnly = true)
    public List<Solicitud> listarTodos() {
        return repositorio.findAll();
    }

    @Transactional(readOnly = true)
    public List<Solicitud> listarTodosPorCliente(Long id) {
        return repositorio.findByClienteId(id);
    }

    @Transactional(readOnly = true)
    public Solicitud obtenerPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con id " + id));
    }

    @Transactional(readOnly = true)
    public Solicitud obtenerPorIdContenedor(Long id) {
        return repositorio.findByContenedorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con id de contenedor " + id));
    }

    @Transactional(readOnly = true)
    public List<Solicitud> obtenerPorClienteId(Long id) {
        return repositorio.findByClienteId(id);
    }

    @Transactional(readOnly = true)
    public Solicitud obtenerPorIdyClienteId(Long id, Long idCliente) {
        return repositorio.findByIdAndClienteId(id, idCliente)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Solicitud no encontrada con id " + id + "de cliente con id " + idCliente));
    }

    @Transactional
    public Solicitud actualizar(Long id, SolicitudDtoIn datos) {
        Solicitud existente = repositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con id " + id));

        if (datos.estado() != null && !datos.estado().equals(existente.getEstado())) {
            Seguimiento seguimiento = new Seguimiento();
            seguimiento.setEstadoAnterior(datos.estado()); // Guardar el NUEVO estado
            seguimiento.setFecha(LocalDateTime.now());
            seguimiento.setComentario("La solicitud se encuentra en estado de " + datos.estado());

            if (existente.getSeguimiento() == null) {
                existente.setSeguimiento(new ArrayList<>());
            }

            existente.getSeguimiento().add(seguimiento);
            existente.setEstado(datos.estado());
        }

        existente
                .setCostoEstimado(datos.costoEstimado() != null ? datos.costoEstimado() : existente.getCostoEstimado());
        existente.setCostoFinal(datos.costoFinal() != null ? datos.costoFinal() : existente.getCostoFinal());

        return repositorio.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            throw new ResourceNotFoundException("Solicitud no encontradacon id" + id);
        }
        repositorio.deleteById(id);
    }

}
