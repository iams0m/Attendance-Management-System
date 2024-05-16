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

    public Commute(Employee employee) {
        this.employee = employee;
        this.date = LocalDate.now();
        this.startedAt = LocalTime.now();
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

    // 퇴근 처리
    public void endCommute(LocalTime endedAt) {
        if (this.endedAt != null) {
            throw new IllegalArgumentException("이미 퇴근 처리되었습니다.");
        }
        this.endedAt = endedAt;
    }
}
