package dev.petrov.entity;

import dev.petrov.dto.usersDto.User;
import jakarta.persistence.*;

@Entity
@Table(name = "registration")
public class RegistrationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private EventEntity event;
    @ManyToOne(cascade = CascadeType.ALL)
    private UserEntity user;

    public RegistrationEntity() {
    }

    public RegistrationEntity(EventEntity event, UserEntity user) {
        this.event = event;
        this.user = user;
    }

    public EventEntity getEvent() {
        return event;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setEvent(EventEntity event) {
        this.event = event;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
