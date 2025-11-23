package com.tpi.depositosservice.exepciones;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
