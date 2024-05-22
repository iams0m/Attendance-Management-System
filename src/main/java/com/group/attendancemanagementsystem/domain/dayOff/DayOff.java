package com.group.attendancemanagementsystem.domain.dayOff;

import com.group.attendancemanagementsystem.domain.employee.Employee;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class DayOff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day_off_id")
    private Long id;

    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    protected DayOff() {
    }

    public DayOff(LocalDate date, Employee employee) {
        this.date = date;
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Employee getEmployee() {
        return employee;
    }
}
