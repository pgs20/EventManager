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
    private String eventStatus;

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
        return eventStatus == null || eventStatus.isEmpty() ? null : EventStatus.valueOf(eventStatus.toUpperCase());
    }

    public boolean isFilterEmpty() {
        return durationMax == 0 &&
                placesMin == 0 &&
                locationId == 0 &&
                (eventStatus == null || eventStatus.isEmpty()) &&
                (name == null || name.isEmpty()) &&
                placesMax == 0 &&
                costMin == 0 &&
                (dateStartAfter == null || dateStartAfter.isEmpty()) &&
                (dateStartBefore == null || dateStartBefore.isEmpty()) &&
                costMax == 0 &&
                durationMin == 0;
    }
}
