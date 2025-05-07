package com.scaler.hrmeetings.service;

import com.scaler.hrmeetings.dto.EmployeeDto;
import com.scaler.hrmeetings.dto.MeetingDto;
import com.scaler.hrmeetings.dto.MeetingPatchDto;
import com.scaler.hrmeetings.dto.MeetingSearchCriteria;
import com.scaler.hrmeetings.mapper.EmployeeMapper;
import com.scaler.hrmeetings.mapper.MeetingMapper;
import com.scaler.hrmeetings.model.Employee;
import com.scaler.hrmeetings.model.Meeting;
import com.scaler.hrmeetings.repository.MeetingRepository;
import com.scaler.hrmeetings.repository.MeetingSpecification;
import com.scaler.hrmeetings.security.JwtUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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

        Stream<Meeting> filteredMeetings = meetings.stream()
                .filter(m -> m.getEmployee().getId().equals(callerId) || m.getManager().getId().equals(callerId));
        return filteredMeetings.map(meetingMapper::toDto).toList();
    }

    public MeetingDto findById(Long id) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Meeting not found"));
        return meetingMapper.toDto(meeting);
    }

    public MeetingDto create(MeetingDto dto) {
        Meeting meeting = getMeetingFromDto(dto);

        return meetingMapper.toDto(meetingRepository.save(meeting));
    }
    public MeetingDto update(MeetingDto dto) {
        Meeting meeting = getMeetingFromDto(dto);

        checkAccess(meeting);

        meeting.setId(dto.getId());

        return meetingMapper.toDto(meetingRepository.save(meeting));
    }

    public void deleteById(Long id) {
        Meeting meeting = meetingRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Meeting not found"));

        checkAccess(meeting);

        meetingRepository.deleteById(id);
    }

    @Transactional
    public MeetingDto finalizeMeeting(Long id) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Meeting not found"));

        checkAccess(meeting);

        List<Meeting> overlappingFinalizedMeetings =
                meetingRepository.findByFinalizedTrueAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                        meeting.getEndTime(), meeting.getStartTime()
                );

        if (!overlappingFinalizedMeetings.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Overlapping finalized meetings exist");
        }

        meeting.setFinalized(true);
        return meetingMapper.toDto(meetingRepository.save(meeting));
    }

    public List<Meeting> searchMeetings(
            String title,
            LocalDateTime start,
            LocalDateTime end,
            Boolean finalized
    ) {
        return meetingRepository.findAll();
    }

    public MeetingDto patch(Long id, MeetingPatchDto dto) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Meeting not found"));

        checkAccess(meeting);

        if (dto.getEmployeeId() != null) {
            Employee employee = employeeMapper.toEntity(employeeService.findById(dto.getEmployeeId()));
            meeting.setEmployee (employee);
        }

        if (dto.getManagerId() != null) {
            Employee manager = employeeMapper.toEntity(employeeService.findById(dto.getManagerId()));
            meeting.setEmployee (manager);
        }

        if (dto.getStartTime() != null) {
            meeting.setStartTime (dto.getStartTime());
        }

        if (dto.getEndTime() != null) {
            meeting.setEndTime (dto.getEndTime());
        }

        if (dto.getTitle() != null) {
            meeting.setTitle (dto.getTitle());
        }

        return meetingMapper.toDto(meetingRepository.save(meeting));
    }

    private Meeting getMeetingFromDto(MeetingDto dto) {
        EmployeeDto employee = employeeService.findById(dto.getEmployeeId());
        EmployeeDto manager = employeeService.findById(dto.getManagerId());

        if (employee.getManager() == true) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Employee should not be a manager");
        }

        if (manager.getManager() != true) {
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

        if (meeting.isFinalized()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Meeting already finalized");
        }
    }

    public List<MeetingDto> searchMeetings(MeetingSearchCriteria criteria) {
        List<Meeting> meetings = meetingRepository.findAll(MeetingSpecification.build(criteria));
        return meetings.stream()
                .map(meetingMapper::toDto)
                .collect(Collectors.toList());
    }
}
