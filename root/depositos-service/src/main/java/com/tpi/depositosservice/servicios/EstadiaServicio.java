package com.tpi.depositosservice.servicios;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commonlib.Enums.TiposTramos;
import com.commonlib.Enums.TiposUbicacion;
import com.commonlib.entidades.Estadia;
import com.tpi.depositosservice.dto.ContenedorDtoOut;
import com.tpi.depositosservice.dto.EstadiaDtoIn;
import com.tpi.depositosservice.dto.UbicacionDtoOut;
import com.tpi.depositosservice.repositorios.EstadiaRepository;
import com.tpi.depositosservice.restcliente.SolicitudesClient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EstadiaServicio {

    private final EstadiaRepository estadiaRepository;
    private final SolicitudesClient solicitudesClient;

    @Transactional
    public Estadia crear(EstadiaDtoIn datos) {
        // Validaciones iniciales
        if (datos == null) {
            throw new IllegalArgumentException("Los datos de la estadía no pueden ser nulos");
        }

        if (datos.idContenedor() == null) {
            throw new IllegalArgumentException("El id del contenedor no puede ser nulo");
        }

        if (datos.idTramo() == null) {
            throw new IllegalArgumentException("El id del tramo no puede ser nulo");
        }

        // 1) Obtengo la solicitud
        var solicitud = solicitudesClient.obtenerSolicitudPorContenedor(datos.idContenedor());
        // 2) Obtener la ruta
        var ruta = solicitudesClient.obtenerRutaPorSolicitud(solicitud.idContedor());
        // 3) Obtener los tramos
        var tramos = solicitudesClient.obtenerTramosPorRuta(ruta.idRuta());
        // 4) Recorrer los tramos, matcheo el que tiene el id
        // Asegurar que el tramo matcheado tenga tramo anterior
        // FechaFin del tramo 1 - FechaInicio del tramo 2 = estadia
        // Asegurarse EL TIPO DEL TRAMO MATCHEADO
        // SIEMPRE SIEMPRE DEPOSITO-DESTINO O DEPOSITO-DEPOSITO
        // O sea que el origen siempre es un Deposito

        // Crear la estadía
        // Setear costo Estadia
        // setear la ubicacion de origen como ubicacion de la estadia
        // recordar que solo almacenamos id
        // FechaHora entrada = FechaHoraFin Tramo 1
        // FechaHora salida = FechaHoraInicio Tramo 2
        // Tramo anterior o TRAMO 1 debe estar finalizado si o si

        var tramoMatcheado = tramos
                .stream()
                .filter(t -> t.id().equals(ruta.idRuta()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Tramo no encontrado"));

        if (tramoMatcheado.id() != datos.idTramo()) {
            throw new IllegalArgumentException("El tramo es incorrecto");
        }

        if (tramoMatcheado.fechaInicio() == null) {
            throw new IllegalArgumentException("El tramo todavia no esta iniciado");
        }

        if (!(tramoMatcheado.tipo() == TiposTramos.DEPOSITO_DEPOSITO
                || tramoMatcheado.tipo() == TiposTramos.DEPOSITO_DESTINO)) {
            throw new IllegalArgumentException("El tramo no es de tipo DESPOSITO-DEPOSITO O DEPOSITO-DESTINO");
        }

        var ubicacionTramoMatch = solicitudesClient.obtenerUbicacionPorId(tramoMatcheado.idOrigen());

        if (ubicacionTramoMatch.tipo().toUpperCase() != TiposUbicacion.DEPOSITO.name()) {
            throw new IllegalArgumentException("La ubicacion de origen no es de tipo DESPOSITO");
        }





        return estadiaRepository.save(estadia);
    }

    public Estadia actualizar(Estadia estadia) {
        if (!estadiaRepository.existsById(estadia.getIdEstadia())) {
            throw new NoSuchElementException("No se encontró la estadía con id: " + estadia.getIdEstadia());
        }

        // Asignamos el Deposito y el ID a la entidad;

        return estadiaRepository.save(estadia);
    }

    public Estadia obtenerPorId(Long id) {
        return estadiaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró la estadía con id: " + id));
    }

    public List<Estadia> listarTodos() {
        return estadiaRepository.findAll();
    }

    public void eliminar(Long id) {
        if (!estadiaRepository.existsById(id)) {
            throw new NoSuchElementException("No se encontró la estadía con id: " + id);
        }
        estadiaRepository.deleteById(id);
    }
}