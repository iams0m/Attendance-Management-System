package com.group.attendancemanagementsystem.service.employee;

import com.group.attendancemanagementsystem.domain.employee.Employee;
import com.group.attendancemanagementsystem.domain.role.Role;
import com.group.attendancemanagementsystem.domain.team.Team;
import com.group.attendancemanagementsystem.dto.employee.request.RegisterEmployeeRequest;
import com.group.attendancemanagementsystem.dto.employee.response.EmployeeListResponse;
import com.group.attendancemanagementsystem.repository.employee.EmployeeRepository;
import com.group.attendancemanagementsystem.repository.team.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public void registerEmployee(RegisterEmployeeRequest request) {

        Team team = teamRepository.findById(request.getTeamName())
                .orElseThrow(IllegalArgumentException::new);

        employeeRepository.save(Employee.builder()
                .name(request.getName())
                .team(team)
                .role(request.getRole())
                .birthday(request.getBirthday())
                .workStartDate(request.getWorkStartDate())
                .build());

        if (request.getRole() == Role.MANAGER) {
            team.setManager(request.getName());
        }

        team.addEmployee();
    }

    public List<EmployeeListResponse> getEmployeeList() {
        return employeeRepository.findAll().stream()
                .map(employee -> new EmployeeListResponse(employee.getName(), employee.getTeamName(),
                        employee.getRole(), employee.getBirthday(), employee.getWorkStartDate()))
                .collect(Collectors.toList());
    }
}
