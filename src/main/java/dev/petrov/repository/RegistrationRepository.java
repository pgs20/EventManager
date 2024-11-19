package dev.petrov.repository;

import dev.petrov.entity.EventEntity;
import dev.petrov.entity.RegistrationEntity;
import dev.petrov.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Long> {
    Optional<RegistrationEntity> findByEventAndUser(EventEntity event, UserEntity user);
}
