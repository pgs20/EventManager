package dev.petrov.service;

import dev.petrov.converter.Converter;
import dev.petrov.dto.Location;
import dev.petrov.dto.LocationDto;
import dev.petrov.entity.LocationEntity;
import dev.petrov.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationsService {

    private final Converter converter;
    private final LocationRepository locationRepository;

    public LocationsService(Converter converter, LocationRepository locationRepository) {
        this.converter = converter;
        this.locationRepository = locationRepository;
    }

    public Location createLocation(Location locationToCreate) {
        LocationEntity locationEntity = locationRepository.save(converter.toEntity(locationToCreate));

        return converter.toDomain(locationEntity);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(converter::toDomain)
                .collect(Collectors.toList());
    }

    public Location deleteLocation(Long locationId) {
        LocationEntity locationEntity = locationRepository.getById(locationId);
        locationRepository.delete(locationEntity);

        return converter.toDomain(locationEntity);
    }

    public Location getLocationById(Long locationId) {
        LocationEntity locationEntity = locationRepository.getById(locationId);

        return converter.toDomain(locationEntity);
    }

    public Location updateLocationById(Long locationId, Location locationToUpdate) {
        locationRepository.updateLocationById(
                locationId,
                locationToUpdate.name(),
                locationToUpdate.address(),
                locationToUpdate.capacity(),
                locationToUpdate.description()
        );

        return getLocationById(locationId);
    }
}
