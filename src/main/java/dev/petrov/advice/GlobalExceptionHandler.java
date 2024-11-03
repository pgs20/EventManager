package dev.petrov.advice;

import dev.petrov.dto.ServerErrorDto;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ServerErrorDto> handleGenericException(EntityNotFoundException e) {
        ServerErrorDto errorDto = new ServerErrorDto(
                "Сущность не найдена",
                e.getMessage(),
                LocalDateTime.now()
        );

        log.error(errorDto.detailedMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ServerErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ServerErrorDto errorDto = new ServerErrorDto(
                "Невалидные данные",
                e.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .collect(Collectors.joining()),
                LocalDateTime.now()
        );

        log.error(errorDto.detailedMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }
}
