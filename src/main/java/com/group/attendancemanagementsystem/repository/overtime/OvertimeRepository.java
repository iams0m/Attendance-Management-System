package com.group.attendancemanagementsystem.repository.overtime;

import com.group.attendancemanagementsystem.domain.commute.Commute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OvertimeRepository extends JpaRepository<Commute, Long> {
    @Query("SELECT c FROM Commute c WHERE c.employee.id = :employeeId AND EXTRACT(YEAR FROM c.startedAt) = :year AND EXTRACT(MONTH FROM c.startedAt) = :month")
    List<Commute> findByEmployeeIdAndMonthAndYear(@Param("employeeId") Long employeeId, @Param("year") int year, @Param("month") int month);
}
