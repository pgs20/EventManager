package dev.petrov.service;

import dev.petrov.converter.Converter;
import dev.petrov.dto.Location;
import dev.petrov.entity.LocationEntity;
import dev.petrov.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.DataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationsService {

    private static final Logger log = LoggerFactory.getLogger(LocationsService.class);
    private final Converter converter;
    private final LocationRepository locationRepository;

    public LocationsService(Converter converter, LocationRepository locationRepository) {
        this.converter = converter;
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocations() {
        log.info("Получение всех локаций");
        return locationRepository.findAll()
                .stream()
                .map(converter::toDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    public Location createLocation(Location locationToCreate) {
        LocationEntity locationEntity = locationRepository.save(converter.toEntity(locationToCreate));
        log.info("Создание локации: " + locationEntity);

        return converter.toDomain(locationEntity);
    }

    public Location getLocationById(Long locationId) {
        LocationEntity locationEntity = locationRepository.getById(locationId);
        log.info("Получение локации по id: " + locationEntity);

        return converter.toDomain(locationEntity);
    }

    public Location deleteLocation(Long locationId) {
        int countDeleteEntity = locationRepository.deleteLocationById(locationId);

        checkLocationEntity(countDeleteEntity, "Не удалось удалить сущность, так как ее не существует");
        log.info("Сущность с id = %d успешно удалена".formatted(locationId));

        return getLocationById(locationId);
    }

    public Location updateLocationById(Long locationId, Location locationToUpdate) {
        int countUpdateEntity = locationRepository.updateLocationById(
                locationId,
                locationToUpdate.name(),
                locationToUpdate.address(),
                locationToUpdate.capacity(),
                locationToUpdate.description()
        );

        checkLocationEntity(countUpdateEntity, "Не удалось обновить сущность, так как ее не существует");
        log.info("Сущность с id = %d успешно обновлена".formatted(locationId));

        return getLocationById(locationId);
    }

    private void checkLocationEntity(int countEntity, String message) {
        if (countEntity == 0) {
            throw new EntityNotFoundException(message);
        }
    }
}
