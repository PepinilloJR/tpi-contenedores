package com.pedidos.service.demo.exepciones;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.commonlib.error.ErrorRequest;

@RestControllerAdvice
public class ExepcionesManejadorGlobal {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorRequest> manejarNoEncontrado(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorRequest(404, ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorRequest> manejarBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorRequest(400, ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorRequest> handleDataIntegrity(DataIntegrityViolationException ex) {
        String detalle = null;
        if (ex.getMostSpecificCause() != null && ex.getMostSpecificCause().getMessage() != null) {
            detalle = ex.getMostSpecificCause().getMessage();
        } else {
            detalle = "Violación de integridad en la base de datos";
        }
        return ResponseEntity.badRequest().body(new ErrorRequest(400, "Violación de datos: " + detalle));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRequest> manejarExcepcionGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorRequest(500, "Error interno del servidor"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleBadJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(new ErrorRequest(400, "JSON inválido: " + ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {

        var errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        var mensaje = "Errores de validación: " + String.join("; ", errores);

        return ResponseEntity
                .badRequest()
                .body(new ErrorRequest(400, mensaje));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body(new ErrorRequest(400, "Valor inválido para: " + ex.getName()));
    }
}
