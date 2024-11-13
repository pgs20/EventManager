package dev.petrov.service;

import dev.petrov.converter.ConverterLocation;
import dev.petrov.dto.locationDto.Location;
import dev.petrov.entity.LocationEntity;
import dev.petrov.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private static final Logger log = LoggerFactory.getLogger(LocationService.class);
    private final ConverterLocation converter;
    private final LocationRepository locationRepository;

    public LocationService(ConverterLocation converter, LocationRepository locationRepository) {
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

        if (countDeleteEntity == 0) {
            throw new EntityNotFoundException("Не удалось удалить сущность, так как ее не существует");
        }

        log.info("Сущность с id={} успешно удалена", locationId);

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

        if (countUpdateEntity == 0) {
            throw new EntityNotFoundException("Не удалось обновить сущность, так как ее не существует");
        }

        log.info("Сущность с id={} успешно обновлена", locationId);

        return getLocationById(locationId);
    }
}
