package com.group.attendancemanagementsystem.repository.dayOff;

import com.group.attendancemanagementsystem.domain.dayOff.DayOff;
import com.group.attendancemanagementsystem.domain.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DayOffRepository extends JpaRepository<DayOff, Long> {
    long countDayOffByEmployee(Employee employee);
    boolean existsDayOffByEmployeeAndDate(Employee employee, LocalDate date);
}
