package com.scaler.hrmeetings.controller;

import com.scaler.hrmeetings.dto.MeetingDto;
import com.scaler.hrmeetings.mapper.MeetingMapper;
import com.scaler.hrmeetings.model.Employee;
import com.scaler.hrmeetings.model.Meeting;
import com.scaler.hrmeetings.security.JwtRole;
import com.scaler.hrmeetings.security.JwtTokenUtil;
import com.scaler.hrmeetings.security.JwtUserDetails;
import com.scaler.hrmeetings.service.EmployeeService;
import com.scaler.hrmeetings.service.MeetingService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    private final MeetingService meetingService;
    private final MeetingMapper meetingMapper;

    public MeetingController(MeetingService meetingService, MeetingMapper meetingMapper) {
        this.meetingService = meetingService;
        this.meetingMapper = meetingMapper;
    }

    @GetMapping
    public List<MeetingDto> getAll() {
        JwtTokenUtil.checkRoleInJwtToken(JwtRole.READ);
        return meetingService.findAll();
    }

    @GetMapping("/{id}")
    public MeetingDto getById(@PathVariable Long id) {
        JwtTokenUtil.checkRoleInJwtToken(JwtRole.READ);
        Meeting meeting = meetingService.findById(id).orElseThrow();
        return meetingMapper.toDto(meeting);
    }

    @PostMapping
    public MeetingDto create(@RequestBody MeetingDto dto) {
        JwtTokenUtil.checkRoleInJwtToken(JwtRole.WRITE);
        return meetingService.create(dto);
    }

    @PutMapping("/{id}")
    public MeetingDto update(@PathVariable Long id, @RequestBody MeetingDto dto) {
        JwtTokenUtil.checkRoleInJwtToken(JwtRole.WRITE);
        return meetingService.update(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        JwtTokenUtil.checkRoleInJwtToken(JwtRole.WRITE);
        Meeting meeting = meetingService.findById(id).orElseThrow();

        JwtUserDetails user = (JwtUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long callerId = user.getCallerId();

        if (!callerId.equals(meeting.getEmployee().getId()) &&
                !callerId.equals(meeting.getManager().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        meetingService.deleteById(id);
    }

    @PostMapping("/{id}/finalize")
    public MeetingDto finalizeMeeting(@PathVariable Long id) {
        JwtTokenUtil.checkRoleInJwtToken(JwtRole.WRITE);
        Meeting finalized = meetingService.finalizeMeeting(id);
        return meetingMapper.toDto(finalized);
    }
}
