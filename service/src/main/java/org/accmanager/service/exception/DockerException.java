package org.accmanager.service.exception;

public class DockerException extends RuntimeException {

    public DockerException(String message, Throwable ex) {
        super(message, ex);
    }
}