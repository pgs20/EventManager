package dev.petrov.validator.date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FutureDateValidator implements ConstraintValidator<FutureDate, String> {
    @Override
    public boolean isValid(String dateString, ConstraintValidatorContext constraintValidatorContext) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
            return dateTime.isAfter(LocalDateTime.now());
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException(e.getMessage(), e.getParsedString(), e.getErrorIndex());
        }
    }
}
