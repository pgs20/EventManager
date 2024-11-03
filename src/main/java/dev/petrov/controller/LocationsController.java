package dev.petrov.controller;

import dev.petrov.converter.Converter;
import dev.petrov.dto.LocationDto;
import dev.petrov.service.LocationsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/locations")
public class LocationsController {

    private final Converter converter;
    private final LocationsService locationsService;

    public LocationsController(Converter converter, LocationsService locationsService) {
        this.converter = converter;
        this.locationsService = locationsService;
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        List<LocationDto> locationDtoList =  locationsService.getAllLocations()
                .stream()
                .map(converter::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(locationDtoList);
    }

    @PostMapping
    public LocationDto createLocation(@Valid @RequestBody LocationDto locationToCreate) {
        return converter.toDto(locationsService.createLocation(converter.toDomain(locationToCreate)));
    }

    @DeleteMapping("/{locationId}")
    public LocationDto deleteLocation(@PathVariable Long locationId) {
        return converter.toDto(locationsService.deleteLocation(locationId));
    }

    @GetMapping("/{locationId}")
    public LocationDto getLocationById(@PathVariable Long locationId) {
        return converter.toDto(locationsService.getLocationById(locationId));
    }

    @PutMapping("/{locationId}")
    public LocationDto updateLocationById(@PathVariable Long locationId, @Valid @RequestBody LocationDto locationToUpdate) {
        return converter.toDto(locationsService.updateLocationById(locationId, converter.toDomain(locationToUpdate)));
    }
}
