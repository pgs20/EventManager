package dev.petrov.validator;

import dev.petrov.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniqueLoginValidator implements ConstraintValidator<UniqueLogin, String> {

    private final UserRepository userRepository;

    public UniqueLoginValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext constraintValidatorContext) {
        return !userRepository.existsByLogin(login);
    }
}
