package dev.petrov.repository;

import dev.petrov.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    boolean existsByLogin(String login);

    Optional<UserEntity> findUserByLogin(String login);
}
