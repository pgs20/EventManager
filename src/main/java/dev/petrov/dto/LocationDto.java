package dev.petrov.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

public class LocationDto {
    @Null
    private final Long id;
    @NotBlank
    @Size(max = 30)
    private final String name;
    @NotBlank
    private final String address;
    @Min(value = 1000)
    private final Integer capacity;
    private final String description;

    public LocationDto(Long id, String name, String address, Integer capacity, String description) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public String getDescription() {
        return description;
    }
}
