package com.tpi.depositosservice.servicios;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commonlib.Enums.EstadosTramo;
import com.commonlib.Enums.TiposTramos;
import com.commonlib.Enums.TiposUbicacion;
import com.commonlib.entidades.Estadia;
import com.tpi.depositosservice.dto.EstadiaDtoIn;
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

        if (datos == null) {
            throw new IllegalArgumentException("Los datos de la estadía no pueden ser nulos");
        }
        if (datos.idContenedor() == null) {
            throw new IllegalArgumentException("El id del contenedor no puede ser nulo");
        }
        if (datos.idTramo() == null) {
            throw new IllegalArgumentException("El id del tramo no puede ser nulo");
        }

        // 1) Obtener la solicitud asociada al contenedor
        var solicitud = solicitudesClient.obtenerSolicitudPorContenedor(datos.idContenedor());

        // 2) Obtener la ruta de la solicitud
        var ruta = solicitudesClient.obtenerRutaPorSolicitud(solicitud.id());

        // 3) Obtener todos los tramos de la ruta
        var tramos = solicitudesClient.obtenerTramosPorRuta(ruta.idRuta());

        // 4) Buscar el tramo especificado en la lista de tramos
        var tramoMatcheado = tramos.stream()
                .filter(t -> t.id().equals(datos.idTramo()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Tramo no encontrado"));

        // 5) Validar que el tramo haya iniciado
        if (tramoMatcheado.fechaInicio() == null) {
            throw new IllegalArgumentException("El tramo todavía no está iniciado");
        }

        // 6) Validar que el tramo sea DEPOSITO-DEPOSITO o DEPOSITO-DESTINO
        // Esto asegura que el origen del tramo sea siempre un deposito
        if (!(TiposTramos.DEPOSITO_DEPOSITO.equals(tramoMatcheado.tipo())
                || TiposTramos.DEPOSITO_DESTINO.equals(tramoMatcheado.tipo()))) {
            throw new IllegalArgumentException("El tramo no es de tipo DEPOSITO-DEPOSITO o DEPOSITO-DESTINO");
        }

        // 7) Verificar que exista un tramo anterior
        if (tramoMatcheado.idTramoAnterior() == null) {
            throw new IllegalArgumentException("El tramo no tiene tramo anterior definido");
        }

        // 8) Obtener el tramo anterior
        var tramoAnterior = solicitudesClient.obtenerTramoPorId(tramoMatcheado.idTramoAnterior());

        // 9) Validar que el tramo anterior este finalizado
        // La estadía se calcula entre el fin del tramo anterior y el inicio del actual
        if (tramoAnterior.fechaFin() == null || !EstadosTramo.FINALIZADO.equals(tramoAnterior.estado())) {
            throw new IllegalArgumentException("El tramo anterior no está finalizado");
        }

        // 10) Validar que el tramo anterior sea ORIGEN-DEPOSITO o DEPOSITO-DEPOSITO
        // Esto asegura que el destino del tramo anterior sea un depósito
        if (!(TiposTramos.ORIGEN_DEPOSITO.equals(tramoAnterior.tipo())
                || TiposTramos.DEPOSITO_DEPOSITO.equals(tramoAnterior.tipo()))) {
            throw new IllegalArgumentException("El tramo anterior no es de tipo ORIGEN-DEPOSITO o DEPOSITO-DEPOSITO");
        }

        // 11) Obtener las ubicaciones para validar continuidad
        var ubicacionTramoMatchOrigen = solicitudesClient.obtenerUbicacionPorId(tramoMatcheado.idOrigen());
        var ubicacionTramoAnteriorDestino = solicitudesClient.obtenerUbicacionPorId(tramoAnterior.idDestino());

        // 12) Validar que el destino del tramo anterior coincida con el origen del
        // tramo actual
        // Esto asegura la continuidad de la ruta
        if (!ubicacionTramoMatchOrigen.id().equals(ubicacionTramoAnteriorDestino.id())) {
            throw new IllegalArgumentException("La ubicación de origen no es la misma que la de destino entre tramos");
        }

        // 13) Validar que la ubicacion sea efectivamente un deposito
        if (!ubicacionTramoMatchOrigen.tipo().equals(TiposUbicacion.DEPOSITO.name())) {
            throw new IllegalArgumentException("La ubicación no es un depósito");
        }

        // 14) crear la estadia
        // - ubicacion, origen del tramo actual (que es el depósito)
        // - fecha entrada, fin del tramo anterior (cuando llegó al depósito)
        // - fecha salida, inicio del tramo actual (cuando sale del depósito)
        var estadia = new Estadia(
                null,
                tramoMatcheado.idOrigen(),
                datos.idContenedor(),
                tramoAnterior.fechaFin(), // fecha de entrada al depósito
                tramoMatcheado.fechaInicio(), // fecha de salida del depósito
                null // costo se calcula a continuación
        );

        // 15) calcular el costo de la estadia según el costo del deposito
        estadia.calcularCosto(ubicacionTramoMatchOrigen.costo());

        // 16) guardar y retornar la estadia
        return estadiaRepository.save(estadia);
    }

    public Estadia obtenerPorId(Long id) {
        return estadiaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró la estadía con id: " + id));
    }

    public List<Estadia> obtenerEstadiasPorContenedor(Long id) {
        return estadiaRepository.findByIdDeposito(id);
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