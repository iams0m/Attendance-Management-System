package com.group.attendancemanagementsystem.service.commute;

import com.group.attendancemanagementsystem.domain.commute.Commute;
import com.group.attendancemanagementsystem.domain.employee.Employee;
import com.group.attendancemanagementsystem.repository.commute.CommuteRepository;
import com.group.attendancemanagementsystem.repository.employee.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class CommuteService {
    private final CommuteRepository commuteRepository;
    private final EmployeeRepository employeeRepository;

    public CommuteService(CommuteRepository commuteRepository, EmployeeRepository employeeRepository) {
        this.commuteRepository = commuteRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public void startedCommute(Long employeeId) {

        // 직원 조회
        Employee employee = getEmployeeById(employeeId);

        // 당일 출근 기록이 없는 경우
        if (!commuteRepository.existsCommuteByEmployeeAndDate(employee, LocalDate.now())) {
            Commute commute = new Commute(employee);
            commuteRepository.save(commute);
        } else {
            throw new IllegalArgumentException("이미 출근한 직원입니다.");
        }
    }

    private Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findEmployeeById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다."));
    }
}
