package dev.petrov.repository;

import dev.petrov.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE LocationEntity l SET l.name = :name, l.address = :address, l.capacity = :capacity, l.description = :description WHERE l.id = :id")
    void updateLocationById(@Param("id") Long id,
                        @Param("name") String name,
                        @Param("address") String address,
                        @Param("capacity") Integer capacity,
                        @Param("description") String description);

}
