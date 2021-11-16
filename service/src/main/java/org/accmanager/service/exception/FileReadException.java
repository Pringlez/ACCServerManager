package org.accmanager.service.exception;

public class FileReadException extends RuntimeException {

    public FileReadException(String message, Throwable ex) {
        super(message, ex);
    }
}