package dev.petrov.repository;

import dev.petrov.entity.EventEntity;
import dev.petrov.entity.RegistrationEntity;
import dev.petrov.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {
    Optional<RegistrationEntity> findByEventAndUser(EventEntity event, UserEntity user);

    @Query("SELECT e FROM EventEntity e JOIN RegistrationEntity r ON e.id = r.event.id WHERE r.user.id = :userId")
    List<EventEntity> findEventsByUserId(@Param("userId") Long userId);
}
