package com.scaler.hrmeetings.dto;

import java.time.LocalDateTime;

public class MeetingSearchCriteria {
    private String title;
    private LocalDateTime startTimeFrom;
    private LocalDateTime endTimeTo;
    private Boolean finalized;

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStartTimeFrom() {
        return startTimeFrom;
    }

    public LocalDateTime getEndTimeTo() {
        return endTimeTo;
    }

    public Boolean getFinalized() {
        return finalized;
    }
}
