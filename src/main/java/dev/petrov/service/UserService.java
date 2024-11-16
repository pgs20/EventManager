package dev.petrov.service;

import dev.petrov.advice.exception.UserAlreadyExistsException;
import dev.petrov.converter.ConverterUser;
import dev.petrov.dto.usersDto.User;
import dev.petrov.dto.usersDto.UserRole;
import dev.petrov.entity.UserEntity;
import dev.petrov.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final ConverterUser converterUser;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserService(ConverterUser converter, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.converterUser = converter;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registrationUser(User user) {
        if (userRepository.existsByLogin(user.getLogin())) {
            throw new UserAlreadyExistsException("Пользователь с логином " + user.getLogin() + " уже существует");
        }

        user.setUserRole(UserRole.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserEntity userEntity = userRepository.save(converterUser.toEntity(user));

        return converterUser.toDomain(userEntity);
    }

    public User getInfoUserById(Integer userId) {
        UserEntity userEntity = Optional.of(userRepository.getById(userId))
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id=" + userId + " не найден"));

        return converterUser.toDomain(userEntity);
    }

    public User findUserByLogin(String login) {
        return converterUser.toDomain(
                userRepository.findUserByLogin(login)
                        .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"))
        );
    }

}
