package dev.petrov.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "events")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name_event", nullable = false)
    private String name;
    @Column(name = "owner_id")
    private String ownerId;
    @Column(name = "max_places", nullable = false)
    private Integer maxPlaces;
    @Column(name = "occupied_places")
    private Integer occupiedPlaces;
    @Column(name = "date_event", nullable = false)
    private String date;
    @Column(name = "cost_event", nullable = false)
    private Integer cost;
    @Column(name = "duration_event", nullable = false)
    private Integer duration;
    @Column(name = "location_id", nullable = false)
    private Long locationId;
    @Column(name = "status_event")
    private String status;

    public EventEntity() {
    }

    public EventEntity(String name, String ownerId, Integer maxPlaces, String date, Integer cost, Integer duration, Long locationId, String status) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setMaxPlaces(Integer maxPlaces) {
        this.maxPlaces = maxPlaces;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
}
