package org.accmanager.service.api;

import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import org.accmanager.service.exception.InstanceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static java.lang.String.format;
import static org.accmanager.service.enums.ExceptionEnum.*;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAdvisor.class);

    @ExceptionHandler(InstanceNotFoundException.class)
    public ResponseEntity<Object> handleInstanceNotFoundException(InstanceNotFoundException ex) {
        LOGGER.error(format(ACC_SERVER_INSTANCE_NOT_FOUND.toString(), ex.getMessage()));
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        LOGGER.error(format(DOCKER_CONTAINER_NOT_FOUND.toString(), ex.getMessage()));
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Object> handleConflictException(ConflictException ex) {
        LOGGER.error(format(DOCKER_CONTAINER_NAME_IN_USE.toString(), ex.getMessage()));
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
}
