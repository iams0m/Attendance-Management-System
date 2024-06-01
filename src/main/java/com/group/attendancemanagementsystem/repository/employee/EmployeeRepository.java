package com.group.attendancemanagementsystem.repository.employee;

import com.group.attendancemanagementsystem.domain.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findEmployeeById(@Param("employeeId") Long employeeId);

    @Query("SELECT e.name FROM Employee e WHERE e.id = :id")
    String findNameById(Long id);
}
