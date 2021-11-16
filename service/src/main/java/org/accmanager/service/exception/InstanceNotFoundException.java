package org.accmanager.service.exception;

public class InstanceNotFoundException extends RuntimeException {

    public InstanceNotFoundException(String message) {
        super(message);
    }
}
