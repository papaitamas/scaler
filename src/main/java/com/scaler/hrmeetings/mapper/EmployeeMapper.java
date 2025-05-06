package com.scaler.hrmeetings.mapper;

import com.scaler.hrmeetings.dto.EmployeeDto;
import com.scaler.hrmeetings.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeDto toDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setPosition(employee.getPosition());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setManager(employee.isManager());
        return dto;
    }

    public Employee toEntity(EmployeeDto dto) {
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setName(dto.getName());
        employee.setPosition(dto.getPosition());
        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setManager(dto.isManager());
        return employee;
    }
}
