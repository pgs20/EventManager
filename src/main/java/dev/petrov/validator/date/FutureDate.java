package dev.petrov.validator.date;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FutureDateValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureDate {
    String message() default "Дата и время должны быть в будущем";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
