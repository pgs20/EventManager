package dev.petrov.service;

import dev.petrov.dto.usersDto.UserRole;
import dev.petrov.entity.UserEntity;
import dev.petrov.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeUser() {
        if (!userRepository.existsByLogin("admin")) {
            userRepository.save(
                    new UserEntity(
                            "admin",
                            passwordEncoder.encode("admin"),
                            UserRole.ADMIN.name()
                    )
            );
        }

        if (!userRepository.existsByLogin("user")) {
            userRepository.save(
                    new UserEntity(
                            "user",
                            passwordEncoder.encode("user"),
                            UserRole.USER.name()
                    )
            );
        }
    }
}
