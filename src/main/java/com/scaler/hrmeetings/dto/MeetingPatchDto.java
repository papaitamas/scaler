package com.scaler.hrmeetings.dto;

import java.time.LocalDateTime;

public class MeetingPatchDto {
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long employeeId;
    private Long managerId;

    // Getters & Setters

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public Long getManagerId() {
        return managerId;
    }
}
