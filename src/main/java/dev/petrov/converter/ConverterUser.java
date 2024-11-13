package dev.petrov.converter;

import dev.petrov.dto.usersDto.User;
import dev.petrov.dto.usersDto.UserDto;
import dev.petrov.dto.usersDto.UserRegistrationDto;
import dev.petrov.dto.usersDto.UserRole;
import dev.petrov.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class ConverterUser {

    public User toDomain(UserRegistrationDto userRegistrationDto) {
        return new User(
                userRegistrationDto.getLogin(),
                userRegistrationDto.getPassword(),
                userRegistrationDto.getAge()
        );
    }

    public User toDomain(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getAge(),
                UserRole.valueOf(userEntity.getRole())
        );
    }

    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.getLogin(),
                user.getPassword(),
                user.getAge(),
                user.getRole().name()
        );
    }

    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getLogin(),
                user.getAge(),
                user.getRole()
        );
    }
}
