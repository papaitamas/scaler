package com.scaler.hrmeetings.mapper;

import com.scaler.hrmeetings.dto.MeetingDto;
import com.scaler.hrmeetings.model.Employee;
import com.scaler.hrmeetings.model.Meeting;
import org.springframework.stereotype.Component;

@Component
public class MeetingMapper {

    public MeetingDto toDto(Meeting meeting) {
        MeetingDto dto = new MeetingDto();
        dto.setId(meeting.getId());
        dto.setTitle(meeting.getTitle());
        dto.setStartTime(meeting.getStartTime());
        dto.setEndTime(meeting.getEndTime());
        dto.setFinalized(meeting.isFinalized());

        if (meeting.getEmployee() != null) {
            dto.setEmployeeId(meeting.getEmployee().getId());
            dto.setEmployeeName(meeting.getEmployee().getName());
        }

        if (meeting.getManager() != null) {
            dto.setManagerId(meeting.getManager().getId());
            dto.setManagerName(meeting.getManager().getName());
        }

        return dto;
    }

    public Meeting toEntity(MeetingDto dto, Employee employee, Employee manager) {
        Meeting meeting = new Meeting();
        meeting.setTitle(dto.getTitle());
        meeting.setStartTime(dto.getStartTime());
        meeting.setEndTime(dto.getEndTime());
        meeting.setFinalized(false);

        meeting.setEmployee(employee);
        meeting.setManager(manager);
        return meeting;
    }
}
