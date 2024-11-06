package dev.petrov.service;

import dev.petrov.converter.Converter;
import dev.petrov.dto.usersDto.User;
import dev.petrov.entity.UserEntity;
import dev.petrov.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final Converter converter;
    private final UserRepository userRepository;

    public UsersService(Converter converter, UserRepository userRepository) {
        this.converter = converter;
        this.userRepository = userRepository;
    }

    public User registrationUser(User user) {
        UserEntity userEntity = userRepository.save(converter.toEntity(user));

        return converter.toDomain(userEntity);
    }

    public User getInfoUserById(Integer userId) {
        UserEntity userEntity = userRepository.getById(userId);

        return converter.toDomain(userEntity);
    }
}
