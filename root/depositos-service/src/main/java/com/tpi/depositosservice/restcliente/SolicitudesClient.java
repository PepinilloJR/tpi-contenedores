package com.tpi.depositosservice.restcliente;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.tpi.depositosservice.dto.TramoDtoOut;
import com.tpi.depositosservice.dto.ContenedorDtoOut;
import com.tpi.depositosservice.dto.RutaDtoOut;
import com.tpi.depositosservice.dto.SolicitudDtoOut;
import com.tpi.depositosservice.dto.UbicacionDtoOut;
import com.tpi.depositosservice.exepciones.ResourceNotFoundException;

@Component
public class SolicitudesClient {
    private final RestClient restClient;

    public SolicitudesClient(RestClient solicitudesClient) {
        this.restClient = solicitudesClient;
    }

    // ===== ubicaciones =====

    public UbicacionDtoOut obtenerUbicacionPorId(Long id) {
        try {
            return restClient.get()
                    .uri("/api/ubicaciones/" + id)
                    .retrieve()
                    .body(UbicacionDtoOut.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Ubicación no encontrada con id " + id);
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el servicio de solicitudes " + e.getMessage());
        }
    }

    // ==== contenedores =====

    public ContenedorDtoOut obtenerContenedorPorId(Long id) {
        try {
            return restClient.get()
                    .uri("/api/contenedores/" + id)
                    .retrieve()
                    .body(ContenedorDtoOut.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Contenedor no encontrado con id " + id);
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el servicio de solicitudes " + e.getMessage());
        }
    }

    // Solicitudes

    public SolicitudDtoOut obtenerSolicitudPorContenedor(Long idContenedor) {
        try {
            return restClient.get()
                    .uri("/api/solicitudes/contenedor/" + idContenedor)
                    .retrieve()
                    .body(SolicitudDtoOut.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Solicitud no encontrada para el contenedor con id " + idContenedor);
        } catch (Exception e) {  
            throw new RuntimeException("Error al comunicarse con el servicio de solicitudes " + e.getMessage());
        }
    }

    // Tramos

    public TramoDtoOut obtenerTramoPorId(Long id) {
        try {
            return restClient.get()
                    .uri("/api/tramos/" + id)
                    .retrieve()
                    .body(TramoDtoOut.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Tramo no encontrado con id " + id);
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el servicio de solicitudes " + e.getMessage());
        }
    }

    public List<TramoDtoOut> obtenerTodosTramosOPorRuta(Long rutaId) {
        try {
            String uri = rutaId != null ? "/api/tramos?rutaId=" + rutaId : "/api/tramos";

            return restClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<TramoDtoOut>>() {
                    });
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("No se encontraron tramos" +
                    (rutaId != null ? " para la ruta con id " + rutaId : ""));
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el servicio de solicitudes " + e.getMessage());
        }
    }

    // Métodos específicos para mayor claridad

    public List<TramoDtoOut> obtenerTodosTramos() {
        return obtenerTodosTramosOPorRuta(null);
    }

    public List<TramoDtoOut> obtenerTramosPorRuta(Long rutaId) {
        if (rutaId == null) {
            throw new IllegalArgumentException("El ID de ruta no puede ser nulo");
        }
        return obtenerTodosTramosOPorRuta(rutaId);
    }

    // rutas

    public RutaDtoOut obtenerRutaPorSolicitud(Long idSolicitud) {
        try {
            return restClient.get()
                    .uri("/api/rutas/solicitud/" + idSolicitud)
                    .retrieve()
                    .body(RutaDtoOut.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Ruta no encontrada para la solicitud con id " + idSolicitud);
        } catch (Exception e) {
            throw new RuntimeException("Error al comunicarse con el servicio de solicitudes " + e.getMessage());
        }
    }

}
