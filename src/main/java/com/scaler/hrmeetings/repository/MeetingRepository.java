package com.scaler.hrmeetings.repository;

import com.scaler.hrmeetings.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long>, JpaSpecificationExecutor<Meeting> {

    List<Meeting> findByFinalizedTrueAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            LocalDateTime end, LocalDateTime start
    );
}
