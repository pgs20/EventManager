package dev.petrov.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record Location(
        Long id,
        String name,
        String address,
        Integer capacity,
        String description
) {
}