package com.group.attendancemanagementsystem.domain.team;

import com.group.attendancemanagementsystem.domain.employee.Employee;
import com.group.attendancemanagementsystem.domain.role.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Team {

    @Id
    @Column(nullable = false)
    private String name;
    private String manager;
    private int memberCount;

    @OneToMany(mappedBy = "team")
    private List<Employee> employees = new ArrayList<>();

    public void addEmployee() {
        this.memberCount++;
    }

    public void setManager(String name) {
        changeRole();
        this.manager = name;
    }

    private void changeRole() {
        if(this.manager == null) return;

        Employee findEmployee = this.employees.stream()
                .filter(employee -> employee.getRole() == Role.MANAGER)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        findEmployee.changeRole(Role.MEMBER);
    }
}
