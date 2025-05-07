package com.scaler.hrmeetings.dto;

import java.time.LocalDate;

public class EmployeePatchDto {
    private String name;
    private String position;
    private LocalDate dateOfBirth;
    private Boolean manager;

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Boolean getManager() {
        return manager;
    }
}
