package dev.petrov.converter;

import dev.petrov.dto.Location;
import dev.petrov.dto.LocationDto;
import dev.petrov.entity.LocationEntity;
import org.springframework.stereotype.Component;

@Component
public class Converter {
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
}
