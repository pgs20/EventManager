package dev.petrov.dto.event;

import java.time.LocalDateTime;

public class EventDto {
    private Integer id;
    private String name;
    private String ownerId;
    private Integer maxPlaces;
    private Integer occupiedPlaces;
    private String date;
    private Integer cost;
    private Integer duration;
    private Long locationId;
    private EventStatus status;

    public EventDto(Integer id, String name, String ownerId, Integer maxPlaces, String date, Integer cost, Integer duration, Long locationId, EventStatus status) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.maxPlaces = maxPlaces;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.locationId = locationId;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Integer getMaxPlaces() {
        return maxPlaces;
    }

    public Integer getOccupiedPlaces() {
        return occupiedPlaces;
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

    public EventStatus getStatus() {
        return status;
    }
}
