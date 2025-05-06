package com.scaler.hrmeetings.controller;

import com.scaler.hrmeetings.dto.EmployeeDto;
import com.scaler.hrmeetings.mapper.EmployeeMapper;
import com.scaler.hrmeetings.model.Employee;
import com.scaler.hrmeetings.security.JwtRole;
import com.scaler.hrmeetings.security.JwtTokenUtil;
import com.scaler.hrmeetings.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;


    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeDto> getAll() {
        JwtTokenUtil.checkRoleInJwtToken(JwtRole.READ);
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    public EmployeeDto getById(@PathVariable Long id) {
        JwtTokenUtil.checkRoleInJwtToken(JwtRole.READ);
        return employeeService.findById(id);
    }

    @PostMapping
    public EmployeeDto create(@RequestBody EmployeeDto dto) {
        JwtTokenUtil.checkRoleInJwtToken(JwtRole.WRITE);
        return employeeService.create(dto);
    }

    @PutMapping("/{id}")
    public EmployeeDto update(@PathVariable Long id, @RequestBody EmployeeDto dto) {
        JwtTokenUtil.checkRoleInJwtToken(JwtRole.WRITE);
        dto.setId(id);
        return employeeService.update(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        JwtTokenUtil.checkRoleInJwtToken(JwtRole.WRITE);
        employeeService.deleteById(id);
    }
}
