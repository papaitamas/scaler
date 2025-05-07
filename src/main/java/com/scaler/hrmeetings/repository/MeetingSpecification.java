package com.scaler.hrmeetings.repository;

import com.scaler.hrmeetings.model.Meeting;
import com.scaler.hrmeetings.dto.MeetingSearchCriteria;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MeetingSpecification {

    public static Specification<Meeting> build(MeetingSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (criteria.getTitle() != null) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + criteria.getTitle().toLowerCase() + "%"));
            }

            if (criteria.getStartTimeFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startTime"), criteria.getStartTimeFrom()));
            }

            if (criteria.getEndTimeTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("endTime"), criteria.getEndTimeTo()));
            }

            if (criteria.getFinalized() != null) {
                predicates.add(cb.equal(root.get("finalized"), criteria.getFinalized()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
