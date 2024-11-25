package dev.petrov.dto.event.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.petrov.validator.date.FutureDate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public abstract class EventRequestDto {
    @NotBlank(message = "Название мероприятия обязательно")
    private String name;
    @NotNull(message = "Кол-во мест обязательно")
    @Min(value = 1, message = "Кол-во мест должно быть положительным числом")
    private Integer maxPlaces;
    @NotNull(message = "Дата и время мероприятия обязательны")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @FutureDate
    private String date;
    @NotNull(message = "Стоимость в рублях обязательна")
    @Min(value = 0, message = "Стоимость должна быть неотрицательным числом")
    private Integer cost;
    @NotNull(message = "Длительность в минутах обязательна")
    @Min(value = 1, message = "Длительность должна быть положительным числом")
    private Integer duration;
    @NotNull(message = "Идентификатор локации, где проходит мероприятие обязателен")
    private Long locationId;

    public String getName() {
        return name;
    }

    public Integer getMaxPlaces() {
        return maxPlaces;
    }

    public String getDate() {
        return date;
    }

    public Integer getCost() {
        return cost;
    }

    public Integer getDuration() {
        return duration;
    }

    public Long getLocationId() {
        return locationId;
    }
}
