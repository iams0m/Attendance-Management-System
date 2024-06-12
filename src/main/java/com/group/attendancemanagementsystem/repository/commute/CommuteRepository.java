package com.group.attendancemanagementsystem.repository.commute;

import com.group.attendancemanagementsystem.domain.commute.Commute;
import com.group.attendancemanagementsystem.domain.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CommuteRepository extends JpaRepository<Commute, Long> {
    boolean existsCommuteByEmployeeAndDate(Employee employee, LocalDate date);
    Optional<Commute> findCommuteByEmployeeAndDate(Employee employee, LocalDate date);
    List<Commute> findCommutesByEmployeeIdAndDateBetween(Long id, LocalDate start, LocalDate end);
}
