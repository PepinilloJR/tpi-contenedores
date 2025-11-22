package com.pedidos.service.demo.exepciones;


public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}