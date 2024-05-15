package com.group.attendancemanagementsystem.service.employee;

import com.group.attendancemanagementsystem.domain.employee.Employee;
import com.group.attendancemanagementsystem.domain.role.Role;
import com.group.attendancemanagementsystem.domain.team.Team;
import com.group.attendancemanagementsystem.dto.employee.request.RegisterEmployeeRequest;
import com.group.attendancemanagementsystem.dto.employee.response.EmployeeFindAllResponse;
import com.group.attendancemanagementsystem.repository.employee.EmployeeRepository;
import com.group.attendancemanagementsystem.repository.team.TeamRepository;
import jakarta.transaction.Transactional;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
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
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 팀입니다."));
        }

        // 만약 직원의 역할이 MANAGER이고, 팀에 이미 MANAGER가 존재 한다면 예외 발생
        if (request.getRole() == Role.MANAGER && team != null && team.hasManager()) {
            throw new IllegalArgumentException("이미 팀에 매니저가 존재합니다.");
        }

        Employee employee = new Employee(
                request.getName(),
                team,
                request.getRole(),
                request.getBirthday(),
                request.getWorkStartDate()
        );

        employeeRepository.save(employee);

        // 만약 회원의 역할이 MANAGER 라면, 팀의 manager 필드 업데이트
        if (employee.getRole() == Role.MANAGER && team != null) {
            team.setManagerName(employee.getName());
            teamRepository.save(team);
        }

        // 팀에 직원 추가
        if (team != null) {
            team.addEmployee(employee);
            teamRepository.save(team);
        }
    }

    @Transactional
    public List<EmployeeFindAllResponse> findAllEmployee() {
        List<Employee> employeeList = employeeRepository.findAll();

        return employeeList.stream()
                .map(employee -> {
                    String teamName = employee.getTeam() != null ? employee.getTeam().getName() : null;
                    return new EmployeeFindAllResponse(
                            employee.getName(),
                            teamName,
                            employee.getRole(),
                            employee.getBirthday(),
                            employee.getWorkStartDate()
                    );
                }).collect(Collectors.toList());
    }
}
