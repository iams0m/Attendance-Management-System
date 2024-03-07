package com.group.attendancemanagementsystem.dto.employee.request;

import com.group.attendancemanagementsystem.domain.role.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RegisterEmployeeRequest {

    private String name;
    private String teamName;
    private Role role;
    private LocalDate birthday;
    private LocalDate workStartDate;
}
