package com.group.attendancemanagementsystem.service.employee;

import com.group.attendancemanagementsystem.domain.employee.Employee;
import com.group.attendancemanagementsystem.domain.role.Role;
import com.group.attendancemanagementsystem.domain.team.Team;
import com.group.attendancemanagementsystem.dto.employee.request.RegisterEmployeeRequest;
import com.group.attendancemanagementsystem.dto.employee.response.EmployeeListResponse;
import com.group.attendancemanagementsystem.repository.employee.EmployeeRepository;
import com.group.attendancemanagementsystem.repository.team.TeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TeamRepository teamRepository;

    public EmployeeService(EmployeeRepository employeeRepository, TeamRepository teamRepository) {
        this.employeeRepository = employeeRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional
    public void registerEmployee(RegisterEmployeeRequest request) {

        Team team = null;

        if (request.getTeamName() != null) {
            team = teamRepository.findByName(request.getTeamName())
                    .orElseThrow(IllegalArgumentException::new);
        }

        Employee employee = new Employee(
                request.getName(),
                team,
                request.getRole(),
                request.getBirthday(),
                request.getWorkStartDate()
        );

        employeeRepository.save(employee);

        if (employee.getRole() == Role.MANAGER) {
            team.setManager(request.getName());
            teamRepository.save(team);
        }
    }

    @Transactional
    public List<EmployeeListResponse> getEmployeeList() {
        List<Employee> employeeList = employeeRepository.findAll();

        return employeeList.stream()
                .map(employee -> {
                    String teamName = employee.getTeam() != null ? employee.getTeam().getName() : null;
                    return new EmployeeListResponse(
                            employee.getName(),
                            teamName,
                            employee.getRole(),
                            employee.getBirthday(),
                            employee.getWorkStartDate()
                    );
                }).collect(Collectors.toList());
    }
}
