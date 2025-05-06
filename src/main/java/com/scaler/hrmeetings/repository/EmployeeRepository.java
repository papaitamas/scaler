package com.scaler.hrmeetings.repository;

import com.scaler.hrmeetings.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Itt később bővítheted pl. kereséssel: findByName, findByPosition, stb.
}
