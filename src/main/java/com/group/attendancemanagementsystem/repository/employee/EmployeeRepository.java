package com.group.attendancemanagementsystem.repository.employee;

import com.group.attendancemanagementsystem.domain.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findEmployeeById(Long employeeId);

    String findNameById(Long id);
}
