package dev.petrov.repository;

import dev.petrov.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Integer>, JpaSpecificationExecutor<EventEntity> {
    @Query("SELECT e FROM EventEntity e WHERE e.owner.id = :ownerId")
    Optional<List<EventEntity>> findByOwnerId(@Param("ownerId") String ownerId);

    @Query("FROM EventEntity e WHERE (:name IS NULL OR e.name LIKE :name) " +
            "AND (:placesMin IS NULL OR e.maxPlaces >= :placesMin) " +
            "AND (:placesMax IS NULL OR e.maxPlaces <= :placesMax) " +
            "AND (:dateStartAfter IS NULL OR e.date >= CAST(:dateStartAfter AS LocalDateTime)) " +
            "AND (:dateStartBefore IS NULL OR e.date <= CAST(:dateStartBefore AS LocalDateTime)) " +
            "AND (:costMin IS NULL OR e.cost >= :costMin) " +
            "AND (:costMax IS NULL OR e.cost <= :costMax) " +
            "AND (:durationMin IS NULL OR e.duration >= :durationMin) " +
            "AND (:durationMax IS NULL OR e.duration <= :durationMax) " +
            "AND (:locationId IS NULL OR e.locationId = :locationId) " +
            "AND (:eventStatus IS NULL OR e.status = :eventStatus)")
    Optional<List<EventEntity>> findByFilters(
            @Param("name") String name,
            @Param("placesMin") Integer placesMin,
            @Param("placesMax") Integer placesMax,
            @Param("dateStartAfter") String dateStartAfter,
            @Param("dateStartBefore") String dateStartBefore,
            @Param("costMin") Double costMin,
            @Param("costMax") Double costMax,
            @Param("durationMin") Integer durationMin,
            @Param("durationMax") Integer durationMax,
            @Param("locationId") Long locationId,
            @Param("eventStatus") String eventStatus);
}
