package com.group.attendancemanagementsystem.domain.commute;

import com.group.attendancemanagementsystem.domain.employee.Employee;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Commute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commute_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalDate date;
    private LocalTime startedAt;
    private LocalTime endedAt;

    protected Commute() {
    }

    public Commute(Employee employee, LocalDate date, LocalTime startedAt) {
        this.employee = employee;
        this.date = date;
        this.startedAt = startedAt;
    }

    public Long getId() {
        return id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartedAt() {
        return startedAt;
    }

    public LocalTime getEndedAt() {
        return endedAt;
    }
}
