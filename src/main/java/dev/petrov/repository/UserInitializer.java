package dev.petrov.repository;

import dev.petrov.dto.usersDto.UserRole;
import dev.petrov.entity.UserEntity;
import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    @Transactional
    public void init() {
        initializeUser();
    }

    private void initializeUser() {
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
