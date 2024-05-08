package com.group.attendancemanagementsystem.dto.employee.response;

import com.group.attendancemanagementsystem.domain.role.Role;

import java.time.LocalDate;

public class EmployeeListResponse {

    private String name;
    private String teamName;
    private Role role;
    private LocalDate birthday;
    private LocalDate workStartDate;

    protected EmployeeListResponse() {
    }

    public EmployeeListResponse(String name, String teamName, Role role, LocalDate birthday, LocalDate workStartDate) {
        this.name = name;
        this.teamName = teamName;
        this.role = role;
        this.birthday = birthday;
        this.workStartDate = workStartDate;
    }

    public String getName() {
        return name;
    }

    public String getTeamName() {
        return teamName;
    }

    public Role getRole() {
        return role;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public LocalDate getWorkStartDate() {
        return workStartDate;
    }
}
