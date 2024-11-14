package dev.petrov.dto.event;

import java.time.LocalDateTime;

public class Event {
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

    public Event(String name, Integer maxPlaces, String date, Integer cost, Integer duration, Long locationId) {
        this.name = name;
        this.maxPlaces = maxPlaces;
        this.date = date;
        this.cost = cost;
        this.duration = duration;
        this.locationId = locationId;
    }

    public Event(Integer id, String name, String ownerId, Integer maxPlaces, String date, Integer cost, Integer duration, Long locationId, EventStatus status) {

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

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setOccupiedPlaces(Integer occupiedPlaces) {
        this.occupiedPlaces = occupiedPlaces;
    }
}
