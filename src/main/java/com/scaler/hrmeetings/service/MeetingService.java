package com.scaler.hrmeetings.service;

import com.scaler.hrmeetings.dto.EmployeeDto;
import com.scaler.hrmeetings.dto.MeetingDto;
import com.scaler.hrmeetings.mapper.EmployeeMapper;
import com.scaler.hrmeetings.mapper.MeetingMapper;
import com.scaler.hrmeetings.model.Meeting;
import com.scaler.hrmeetings.repository.MeetingRepository;
import com.scaler.hrmeetings.security.JwtUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final EmployeeService employeeService;
    private final MeetingMapper meetingMapper;
    private final EmployeeMapper employeeMapper;

    public MeetingService(
            MeetingRepository meetingRepository,
            EmployeeService employeeService,
            MeetingMapper meetingMapper,
            EmployeeMapper employeeMapper) {
        this.meetingRepository = meetingRepository;
        this.employeeService = employeeService;
        this.meetingMapper = meetingMapper;
        this.employeeMapper = employeeMapper;
    }

    public List<MeetingDto> findAll() {
        JwtUserDetails user = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long callerId = user.getCallerId();

        List<Meeting> meetings = meetingRepository.findAll();

        Stream<Meeting> filteredMeetings = meetings
                .stream()
                .filter(m -> m.getEmployee().getId().equals(callerId) || m.getManager().getId().equals(callerId));
        return filteredMeetings.map(meetingMapper::toDto).toList();
    }

    public Optional<Meeting> findById(Long id) {
        return meetingRepository.findById(id);
    }

    public MeetingDto create(MeetingDto dto) {
        Meeting meeting = getMeetingFromDto(dto);

        return meetingMapper.toDto(meetingRepository.save(meeting));
    }
    public MeetingDto update(MeetingDto dto) {
        Meeting meeting = getMeetingFromDto(dto);
        checkAccess(meeting);

        if (meeting.isFinalized()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Meeting already finalized");
        }

        meeting.setId(dto.getId());

        return meetingMapper.toDto(meetingRepository.save(meeting));
    }

    public void deleteById(Long meetingId) {
        Meeting meeting = meetingRepository
                .findById(meetingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Meeting not found"));

        checkAccess(meeting);

        meetingRepository.deleteById(meetingId);
    }

    @Transactional
    public Meeting finalizeMeeting(Long meetingId) {
        Meeting meeting = meetingRepository
                .findById(meetingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Meeting not found"));

        if (meeting.isFinalized()) {
            throw new RuntimeException("Meeting already finalized");
        }

        List<Meeting> overlappingFinalizedMeetings =
                meetingRepository.findByFinalizedTrueAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                        meeting.getEndTime(), meeting.getStartTime()
                );

        if (!overlappingFinalizedMeetings.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Overlapping finalized meetings exist");
        }

        meeting.setFinalized(true);
        return meetingRepository.save(meeting);
    }

    public List<Meeting> searchMeetings(
            String title,
            LocalDateTime start,
            LocalDateTime end,
            Boolean finalized
    ) {
        return meetingRepository.findAll();
    }

    private Meeting getMeetingFromDto(MeetingDto dto) {
        EmployeeDto employee = employeeService.findById(dto.getEmployeeId());
        EmployeeDto manager = employeeService.findById(dto.getManagerId());

        if (employee.isManager()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Employee should not be a manager");
        }

        if (!manager.isManager()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Manager should be a valid manager");
        }
        return meetingMapper.toEntity(dto, employeeMapper.toEntity(employee), employeeMapper.toEntity(manager));
    }

    private void checkAccess(Meeting meeting) {
        JwtUserDetails user = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long callerId = user.getCallerId();

        if (    !callerId.equals(meeting.getEmployee().getId()) &&
                !callerId.equals(meeting.getManager().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only meeting participants can access this meeting");
        }
    }

}
