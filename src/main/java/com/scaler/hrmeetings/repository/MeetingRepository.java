package com.scaler.hrmeetings.repository;

import com.scaler.hrmeetings.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findByFinalized(boolean finalized);

    List<Meeting> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    List<Meeting> findByTitleContainingIgnoreCase(String title);

    List<Meeting> findByFinalizedTrueAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            LocalDateTime end, LocalDateTime start
    );

    List<Meeting> findByEmployeeIdOrManagerId(Long employeeId, Long managerId);
}
