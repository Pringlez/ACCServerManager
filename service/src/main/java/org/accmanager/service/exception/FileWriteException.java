package org.accmanager.service.exception;

public class FileWriteException extends RuntimeException {

    public FileWriteException(String message, Throwable ex) {
        super(message, ex);
    }
}