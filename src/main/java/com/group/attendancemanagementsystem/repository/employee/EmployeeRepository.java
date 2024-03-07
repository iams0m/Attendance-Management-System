package com.group.attendancemanagementsystem.repository.employee;

import com.group.attendancemanagementsystem.domain.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
