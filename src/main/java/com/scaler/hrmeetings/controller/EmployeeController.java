package com.scaler.hrmeetings.controller;

import com.scaler.hrmeetings.dto.EmployeeDto;
import com.scaler.hrmeetings.dto.EmployeePatchDto;
import com.scaler.hrmeetings.security.JwtRole;
import com.scaler.hrmeetings.security.JwtTokenUtil;
import com.scaler.hrmeetings.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeDto> getAll() {
        JwtTokenUtil.checkRoleInToken(JwtRole.READ);
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    public EmployeeDto getById(@PathVariable Long id) {
        JwtTokenUtil.checkRoleInToken(JwtRole.READ);
        return employeeService.findById(id);
    }

    @PostMapping
    public EmployeeDto create(@RequestBody EmployeeDto dto) {
        JwtTokenUtil.checkRoleInToken(JwtRole.WRITE);
        return employeeService.create(dto);
    }

    @PutMapping("/{id}")
    public EmployeeDto update(@PathVariable Long id, @RequestBody EmployeeDto dto) {
        JwtTokenUtil.checkRoleInToken(JwtRole.WRITE);
        dto.setId(id);
        return employeeService.update(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        JwtTokenUtil.checkRoleInToken(JwtRole.WRITE);
        employeeService.deleteById(id);
    }

    @PatchMapping("/{id}")
    public EmployeeDto patch(@PathVariable Long id, @RequestBody EmployeePatchDto patchDto) {
        JwtTokenUtil.checkRoleInToken(JwtRole.WRITE);
        return employeeService.patch(id, patchDto);
    }
}
