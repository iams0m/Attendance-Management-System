package com.group.attendancemanagementsystem.dto.employee.response;

import com.group.attendancemanagementsystem.domain.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class EmployeeListResponse {

    private String name;
    private String teamName;
    private Role role;
    private LocalDate birthday;
    private LocalDate workStartDate;

}
