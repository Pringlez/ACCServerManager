package org.accmanager.service.exception;

public class DirectoryReadException extends RuntimeException {

    public DirectoryReadException(String message, Throwable ex) {
        super(message, ex);
    }
}