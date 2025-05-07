package com.scaler.hrmeetings.controller;

import com.scaler.hrmeetings.dto.MeetingDto;
import com.scaler.hrmeetings.dto.MeetingPatchDto;
import com.scaler.hrmeetings.mapper.MeetingMapper;
import com.scaler.hrmeetings.dto.MeetingSearchCriteria;
import com.scaler.hrmeetings.security.JwtRole;
import com.scaler.hrmeetings.security.JwtTokenUtil;
import com.scaler.hrmeetings.service.MeetingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping
    public List<MeetingDto> getAll() {
        JwtTokenUtil.checkRoleInToken(JwtRole.READ);
        return meetingService.findAll();
    }

    @GetMapping("/{id}")
    public MeetingDto getById(@PathVariable Long id) {
        JwtTokenUtil.checkRoleInToken(JwtRole.READ);
        return meetingService.findById(id);
    }

    @PostMapping
    public MeetingDto create(@RequestBody MeetingDto dto) {
        JwtTokenUtil.checkRoleInToken(JwtRole.WRITE);
        return meetingService.create(dto);
    }

    @PutMapping("/{id}")
    public MeetingDto update(@PathVariable Long id, @RequestBody MeetingDto dto) {
        JwtTokenUtil.checkRoleInToken(JwtRole.WRITE);
        return meetingService.update(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        JwtTokenUtil.checkRoleInToken(JwtRole.WRITE);
        meetingService.deleteById(id);
    }

    @PostMapping("/{id}/finalize")
    public MeetingDto finalizeMeeting(@PathVariable Long id) {
        JwtTokenUtil.checkRoleInToken(JwtRole.WRITE);
        return meetingService.finalizeMeeting(id);
    }

    @PatchMapping("/{id}")
    public MeetingDto patch(@PathVariable Long id, @RequestBody MeetingPatchDto patchDto) {
        JwtTokenUtil.checkRoleInToken(JwtRole.WRITE);
        return meetingService.patch(id, patchDto);
    }

    @PostMapping("/search")
    public List<MeetingDto> searchMeetings(@RequestBody MeetingSearchCriteria criteria) {
        JwtTokenUtil.checkRoleInToken(JwtRole.READ);
        return meetingService.searchMeetings(criteria);
    }
}
