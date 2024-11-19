package dev.petrov.dto.event.request;

import dev.petrov.dto.event.EventStatus;

public class EventSearchRequestDto {
    private String name;
    private Integer placesMin;
    private Integer placesMax;
    private String dateStartAfter;
    private String dateStartBefore;
    private Double costMin;
    private Double costMax;
    private Integer durationMin;
    private Integer durationMax;
    private Long locationId;
    private EventStatus eventStatus;

    public String getName() {
        return name;
    }

    public Integer getPlacesMin() {
        return placesMin;
    }

    public Integer getPlacesMax() {
        return placesMax;
    }

    public String getDateStartAfter() {
        return dateStartAfter;
    }

    public String getDateStartBefore() {
        return dateStartBefore;
    }

    public Double getCostMin() {
        return costMin;
    }

    public Double getCostMax() {
        return costMax;
    }

    public Integer getDurationMin() {
        return durationMin;
    }

    public Integer getDurationMax() {
        return durationMax;
    }

    public Long getLocationId() {
        return locationId;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }
}
