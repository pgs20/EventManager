package dev.petrov.converter;

import dev.petrov.dto.locationDto.Location;
import dev.petrov.dto.locationDto.LocationDto;
import dev.petrov.dto.usersDto.User;
import dev.petrov.dto.usersDto.UserDto;
import dev.petrov.dto.usersDto.UserRegistrationDto;
import dev.petrov.entity.LocationEntity;
import dev.petrov.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class Converter {
    //Для Location
    public LocationDto toDto(Location location) {
        return new LocationDto(
                location.id(),
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }

    public Location toDomain(LocationDto locationDto) {
        return new Location(
                locationDto.getId(),
                locationDto.getName(),
                locationDto.getAddress(),
                locationDto.getCapacity(),
                locationDto.getDescription()
        );
    }

    public Location toDomain(LocationEntity locationEntity) {
        return new Location(
                locationEntity.getId(),
                locationEntity.getName(),
                locationEntity.getAddress(),
                locationEntity.getCapacity(),
                locationEntity.getDescription()
        );
    }

    public LocationEntity toEntity(Location location) {
        return new LocationEntity(
                location.name(),
                location.address(),
                location.capacity(),
                location.description()
        );
    }

    //Для Users
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
                userEntity.getAge()
        );
    }

    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.getLogin(),
                user.getPasswordHash(),
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
