package dev.petrov.controller;

import dev.petrov.converter.ConverterLocation;
import dev.petrov.dto.locationDto.LocationDto;
import dev.petrov.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/locations")
public class LocationsController {

    private final ConverterLocation converter;
    private final LocationService locationsService;

    public LocationsController(ConverterLocation converter, LocationService locationsService) {
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
    public ResponseEntity<LocationDto> createLocation(@Valid @RequestBody LocationDto locationToCreate) {
        return ResponseEntity.ok().body(
                converter.toDto(
                        locationsService.createLocation(
                                converter.toDomain(
                                        locationToCreate)
                        )
                )
        );
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<LocationDto> deleteLocation(@PathVariable Long locationId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                converter.toDto(
                        locationsService.deleteLocation(
                                locationId)
                )
        );
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> getLocationById(@PathVariable Long locationId) {
        return ResponseEntity.ok().body(
                converter.toDto(
                        locationsService.getLocationById(
                                locationId)
                )
        );
    }

    @PutMapping("/{locationId}")
    public ResponseEntity<LocationDto> updateLocationById(@PathVariable Long locationId, @Valid @RequestBody LocationDto locationToUpdate) {
        return ResponseEntity.ok().body(
                converter.toDto(
                        locationsService.updateLocationById(
                                locationId, converter.toDomain(locationToUpdate)
                        )
                )
        );
    }
}
