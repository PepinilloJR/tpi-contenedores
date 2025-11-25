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
import org.springframework.web.client.HttpClientErrorException;

import com.commonlib.entidades.Ruta;
import com.commonlib.error.ErrorRequest;
import com.pedidos.service.demo.dto.RutaDtoIn;
import com.pedidos.service.demo.dto.RutaDtoOut;
import com.pedidos.service.demo.dto.RutaTentativaDtoIn;
import com.pedidos.service.demo.dto.RutaTentativaDtoOut;
import com.pedidos.service.demo.exepciones.ConflictException;
import com.pedidos.service.demo.exepciones.ResourceNotFoundException;
import com.pedidos.service.demo.servicios.RutaServicio;

import io.swagger.v3.oas.annotations.Operation;

// Hacer una interfaz de uso comun para el manejo de los dto

@RestController
@RequestMapping("/api/rutas")
public class RutaControlador {
    private final RutaServicio servicio;

    public RutaControlador(RutaServicio servicio) {
        this.servicio = servicio;
    }
    /*
     * @Operation(summary = "Crear una Ruta", description = "Crea una Ruta")
     * 
     * @PostMapping
     * public ResponseEntity<RutaDto> crear(@RequestBody RutaDto rutaDto) {
     * 
     * 
     * Ruta rutaEntidad = DtoHandler.convertirRutaEntidad(rutaDto);
     * Ruta rutaCreada = servicio.crear(rutaEntidad);
     * return
     * ResponseEntity.status(201).body(DtoHandler.convertirRutaDto(rutaCreada));
     * }
     */

    @Operation(summary = "Finalizar solicitud de una ruta", description = "Finaliza la solicitud de una ruta")
    @PutMapping("/solicitud/{id}/finalizar")
    public ResponseEntity<?> finalizar(@PathVariable Long id) {

        Ruta rutaActualizada;
        try {
            rutaActualizada = servicio.finalizaRuta(id);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorRequest(404, e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorRequest(400, e.getMessage()));
        } catch (ConflictException e) {
            return ResponseEntity.status(409).body(new ErrorRequest(409, e.getMessage()));
        } catch (HttpClientErrorException.NotFound e) {
            return ResponseEntity.status(404).body(e.getResponseBodyAs(ErrorRequest.class));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorRequest(500, e.getMessage()));
        }

        // pasar tiempos a Long para expresarlo en dias
        Long Estimado = null;
        Long Real = null;

        if (rutaActualizada.getTiempoEstimado() != null) {
            Estimado = Math.round(rutaActualizada.getTiempoEstimado() / 86400.0);
            if (Estimado == 0L) {
                Estimado = 1L;
            }
        }
        if (rutaActualizada.getTiempoReal() != null) {
            Real = Math.round(rutaActualizada.getTiempoReal() / 86400.0);
            if (Real == 0L) {
                Real = 1L;
            }
        }

        RutaDtoOut rutaDtoOut = new RutaDtoOut(
                rutaActualizada.getId(),
                rutaActualizada.getDistanciaTotal(),
                rutaActualizada.getSolicitud() != null ? rutaActualizada.getSolicitud().getId() : null,
                Estimado,
                Real,
                rutaActualizada.getCantidadDepositos(),
                rutaActualizada.getCantidadTramos());
        return ResponseEntity.ok(rutaDtoOut);
    }

    // Recordar siempre lo de actualizacion parcial

    @Operation(summary = "Actualizar una Ruta", description = "Actualiza una Ruta")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody RutaDtoIn rutaDto) {

        Ruta rutaActualizada;

        rutaActualizada = servicio.actualizar(id, rutaDto);

        // pasar tiempos a Long para expresarlo en dias
        Long Estimado = null;
        Long Real = null;

        if (rutaActualizada.getTiempoEstimado() != null) {
            Estimado = Math.round(rutaActualizada.getTiempoEstimado() / 86400.0);
            if (Estimado == 0L) {
                Estimado = 1L;
            }
        }
        if (rutaActualizada.getTiempoReal() != null) {
            Real = Math.round(rutaActualizada.getTiempoReal() / 86400.0);
            if (Real == 0L) {
                Real = 1L;
            }
        }

        RutaDtoOut rutaDtoOut = new RutaDtoOut(
                rutaActualizada.getId(),
                rutaActualizada.getDistanciaTotal(),
                rutaActualizada.getSolicitud() != null ? rutaActualizada.getSolicitud().getId() : null,
                Estimado,
                Real,
                rutaActualizada.getCantidadDepositos(),
                rutaActualizada.getCantidadTramos());
        return ResponseEntity.ok(rutaDtoOut);
    }

    /// api/rutas/solicitud/{idSolicitud}

    @Operation(summary = "Obtener una Ruta segun solicitud", description = "Obtiene una Ruta dada segun el id de su solicitud")
    @GetMapping("/solicitud/{idSolicitud}")
    public ResponseEntity<?> obtenerPorSolicitud(@PathVariable Long idSolicitud) {
        Ruta ruta;
        try {
            ruta = servicio.obtenerPorIdSolicitud(idSolicitud);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorRequest(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorRequest(500, e.getMessage()));
        }

        // pasar tiempos a Long para expresarlo en dias
        Long Estimado = null;
        Long Real = null;

        if (ruta.getTiempoEstimado() != null) {
            Estimado = Math.round(ruta.getTiempoEstimado() / 86400.0);
            if (Estimado == 0L) {
                Estimado = 1L;
            }
        }
        if (ruta.getTiempoReal() != null) {
            Real = Math.round(ruta.getTiempoReal() / 86400.0);
            if (Real == 0L) {
                Real = 1L;
            }
        }

        RutaDtoOut rutaDtoOut = new RutaDtoOut(
                ruta.getId(),
                ruta.getDistanciaTotal(),
                ruta.getSolicitud() != null ? ruta.getSolicitud().getId() : null,
                Estimado,
                Real,
                ruta.getCantidadDepositos(),
                ruta.getCantidadTramos());
        return ResponseEntity.ok(rutaDtoOut);
    }

    @Operation(summary = "Obtener una Ruta", description = "Obtiene una Ruta dada segun id")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        Ruta ruta;
        try {
            ruta = servicio.obtenerPorId(id);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorRequest(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorRequest(500, e.getMessage()));
        }

        // pasar tiempos a Long para expresarlo en dias
        Long Estimado = null;
        Long Real = null;

        if (ruta.getTiempoEstimado() != null) {
            Estimado = Math.round(ruta.getTiempoEstimado() / 86400.0);
            if (Estimado == 0L) {
                Estimado = 1L;
            }
        }
        if (ruta.getTiempoReal() != null) {
            Real = Math.round(ruta.getTiempoReal() / 86400.0);
            if (Real == 0L) {
                Real = 1L;
            }
        }

        RutaDtoOut rutaDtoOut = new RutaDtoOut(
                ruta.getId(),
                ruta.getDistanciaTotal(),
                ruta.getSolicitud() != null ? ruta.getSolicitud().getId() : null,
                Estimado,
                Real,
                ruta.getCantidadDepositos(),
                ruta.getCantidadTramos());
        return ResponseEntity.ok(rutaDtoOut);
    }

    @Operation(summary = "Obtener todas las Ruta", description = "Obtiene todas las Rutas")
    @GetMapping
    public ResponseEntity<List<RutaDtoOut>> obtenerTodos() {

        List<Ruta> lista = servicio.listarTodos();
        List<RutaDtoOut> dtos = lista.stream().map((r) -> {
            Long Estimado = null;
            Long Real = null;

            if (r.getTiempoEstimado() != null) {
                Estimado = Math.round(r.getTiempoEstimado() / 86400.0);
                if (Estimado == 0L) {
                    Estimado = 1L;
                }
            }
            if (r.getTiempoReal() != null) {
                Real = Math.round(r.getTiempoReal() / 86400.0);
                if (Real == 0L) {
                    Real = 1L;
                }
            }

            return new RutaDtoOut(
                    r.getId(),
                    r.getDistanciaTotal(),
                    r.getSolicitud() != null ? r.getSolicitud().getId() : null,
                    Estimado,
                    Real,
                    r.getCantidadDepositos(),
                    r.getCantidadTramos());

        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/tentativas")
    public ResponseEntity<?> guardarTentativas(@RequestBody RutaTentativaDtoIn dto) {
        try {
            List<RutaTentativaDtoOut> rutas = servicio.crearRutasTentativas(dto);
            return ResponseEntity.ok(rutas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorRequest(400, e.getMessage()));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorRequest(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorRequest(500, e.getMessage()));
        }
    }

    @PutMapping("/{id_ruta}/solicitud/{id_solicitud}")
    public ResponseEntity<?> asignarRuta(@PathVariable Long id_solicitud, @PathVariable Long id_ruta) {
        Ruta ruta = servicio.asignarSolicitud(id_ruta, id_solicitud);

        Long Estimado = null;
        Long Real = null;

        if (ruta.getTiempoEstimado() != null) {
            Estimado = Math.round(ruta.getTiempoEstimado() / 86400.0);
            if (Estimado == 0L) {
                Estimado = 1L;
            }
        }
        if (ruta.getTiempoReal() != null) {
            Real = Math.round(ruta.getTiempoReal() / 86400.0);
            if (Real == 0L) {
                Real = 1L;
            }
        }

        RutaDtoOut rutaDtoOut = new RutaDtoOut(
                ruta.getId(),
                ruta.getDistanciaTotal(),
                ruta.getSolicitud() != null ? ruta.getSolicitud().getId() : null,
                Estimado,
                Real,
                ruta.getCantidadDepositos(),
                ruta.getCantidadTramos());

        return ResponseEntity.ok(rutaDtoOut);
    }

    @Operation(summary = "Elimina una Ruta", description = "Elimina una Ruta dada segun id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
