package dev.petrov.specification;

import dev.petrov.dto.event.request.EventSearchRequestDto;
import dev.petrov.entity.EventEntity;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

public class EventSpecification {
    public static Specification<EventEntity> filterByDto(EventSearchRequestDto eventSearchDto) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (eventSearchDto.getName() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(root.get("name"), "%" + eventSearchDto.getName() + "%"));
            }

            if (eventSearchDto.getPlacesMin() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("maxPlaces"), eventSearchDto.getPlacesMin()));
            }

            if (eventSearchDto.getPlacesMax() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("maxPlaces"), eventSearchDto.getPlacesMax()));
            }

            if (eventSearchDto.getDateStartAfter() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("date"), eventSearchDto.getDateStartAfter()));
            }

            if (eventSearchDto.getDateStartBefore() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("date"), eventSearchDto.getDateStartBefore()));
            }

            if (eventSearchDto.getCostMin() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("cost"), eventSearchDto.getCostMin()));
            }

            if (eventSearchDto.getCostMax() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("cost"), eventSearchDto.getCostMax()));
            }

            if (eventSearchDto.getDurationMin() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThanOrEqualTo(root.get("duration"), eventSearchDto.getDurationMin()));
            }

            if (eventSearchDto.getDurationMax() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.lessThanOrEqualTo(root.get("duration"), eventSearchDto.getDurationMax()));
            }

            if (eventSearchDto.getLocationId() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("locationId"), eventSearchDto.getLocationId()));
            }

            if (eventSearchDto.getEventStatus() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("status"), eventSearchDto.getEventStatus().toString()));
            }

            return predicate;
        };
    }
}
