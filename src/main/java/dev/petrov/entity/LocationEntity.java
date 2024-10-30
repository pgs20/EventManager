package dev.petrov.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "location")
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name_location", nullable = false)
    private String name;
    @Column(name = "address_location", nullable = false)
    private String address;
    @Column(name = "capacity_location", nullable = false)
    private Integer capacity;
    @Column(name = "description_location", nullable = false)
    private String description;

    public LocationEntity() {
    }

    public LocationEntity(String name, String address, Integer capacity, String description) {
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

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "LocationEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", capacity=" + capacity +
                ", description='" + description + '\'' +
                '}';
    }
}
