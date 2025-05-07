package com.scaler.hrmeetings.service;

import com.scaler.hrmeetings.dto.EmployeeDto;
import com.scaler.hrmeetings.dto.EmployeePatchDto;
import com.scaler.hrmeetings.mapper.EmployeeMapper;
import com.scaler.hrmeetings.model.Employee;
import com.scaler.hrmeetings.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    public List<EmployeeDto> findAll() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }

    public EmployeeDto findById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee not found"));
        return employeeMapper.toDto(employee);
    }

    public EmployeeDto create(EmployeeDto dto) {
        Employee employee = employeeMapper.toEntity(dto);
        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    public EmployeeDto update(EmployeeDto dto) {
        if (dto.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID");
        }
        Employee employee = employeeMapper.toEntity(dto);
        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    public EmployeeDto patch(Long employeeId, EmployeePatchDto dto) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));

        if (dto.getName() != null) {
            employee.setName(dto.getName());
        }
        if (dto.getPosition() != null) {
            employee.setPosition(dto.getPosition());
        }
        if (dto.getDateOfBirth() != null) {
            employee.setDateOfBirth(dto.getDateOfBirth());
        }

        if(dto.getManager() != null) {
            employee.setManager(dto.getManager());
        }

        return employeeMapper.toDto(employeeRepository.save(employee));
    }
}
