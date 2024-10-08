package org.itone.trello.taskservice.controller.advice;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import lombok.RequiredArgsConstructor;
import org.itone.trello.taskservice.controller.ProjectController;
import org.itone.trello.taskservice.util.exception.NotFoundException;
import org.itone.trello.taskservice.util.exception.user.EmailAlreadyExistsException;
import org.itone.trello.taskservice.util.exception.user.InvalidDataException;
import org.itone.trello.taskservice.util.exception.user.WrongPasswordException;
import org.slf4j.LoggerFactory;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ErrorMessage> notFoundException(NotFoundException exc) {
        logger.error(exc.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exc.getMessage()));
    }

    @ExceptionHandler(InvalidDataException.class)
    ResponseEntity<ErrorMessage> invalidDataException(InvalidDataException exc) {
        logger.error(exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exc.getMessage()));
    }

    @ExceptionHandler(WrongPasswordException.class)
    ResponseEntity<ErrorMessage> wrongPasswordException(WrongPasswordException exc) {
        logger.error(exc.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorMessage(exc.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    ResponseEntity<ErrorMessage> constraintViolationException(EmailAlreadyExistsException exc) {
        logger.error(exc.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorMessage(exc.getMessage()));
    }
}
