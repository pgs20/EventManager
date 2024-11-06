package dev.petrov.repository;

import dev.petrov.dto.usersDto.UserRole;
import dev.petrov.entity.UserEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserInitializer {

    private final UserRepository userRepository;

    public UserInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
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
                            "admin",
                            UserRole.ADMIN.name()
                    )
            );
        }

        if (!userRepository.existsByLogin("user")) {
            userRepository.save(
                    new UserEntity(
                            "user",
                            "user",
                            UserRole.USER.name()
                    )
            );
        }
    }
}
