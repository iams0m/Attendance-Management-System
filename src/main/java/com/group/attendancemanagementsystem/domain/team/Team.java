package com.group.attendancemanagementsystem.domain.team;

import com.group.attendancemanagementsystem.domain.employee.Employee;
import com.group.attendancemanagementsystem.domain.role.Role;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String manager;

    @OneToMany(mappedBy = "team")
    List<Employee> employees = new ArrayList<>();

    private Long employeeCount;

    protected Team() {
    }

    public Team(String name) {
        this.name = name;
        this.employeeCount = 0L;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getManager() {
        return manager;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public Long getEmployeeCount() {
        return employeeCount;
    }

    public boolean hasManager() {
        return employees.stream().anyMatch(employee -> employee.getRole() == Role.MANAGER);
    }

    public void setManagerName(String manager) {
        this.manager = manager;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
        employeeCount++;
    }




}
