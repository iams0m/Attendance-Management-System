package com.group.attendancemanagementsystem.service.dayOff;

import com.group.attendancemanagementsystem.domain.dayOff.DayOff;
import com.group.attendancemanagementsystem.domain.employee.Employee;
import com.group.attendancemanagementsystem.dto.dayOff.request.RegisterDayOffRequest;
import com.group.attendancemanagementsystem.repository.dayOff.DayOffRepository;
import com.group.attendancemanagementsystem.repository.employee.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class DayOffService {

    private static final long FRESHMAN_DAY_OFF_COUNT = 11;
    private static final long DAY_OFF_COUNT = 15;

    private final DayOffRepository dayOffRepository;
    private final EmployeeRepository employeeRepository;

    public DayOffService(DayOffRepository dayOffRepository, EmployeeRepository employeeRepository) {
        this.dayOffRepository = dayOffRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public void registerDayOff(RegisterDayOffRequest request) {

        // 직원 조회
        Employee employee = getEmployeeById(request.getId());

        // 연차 정책 확인
        int registrationDate = getRegistrationDate(employee);
        long daysBetween = calculateDaysBetween(LocalDate.now(), request.getDate());

        // 남은 연차 수 확인
        long countedDayOff = countRemainingDayOff(employee);

        if (isFreshMan(employee) && isDayOffFullyUsed(FRESHMAN_DAY_OFF_COUNT, countedDayOff)) {
            throw new IllegalArgumentException("이미 11개의 연차를 모두 사용하였습니다.");
        } else if (!isFreshMan(employee) && isDayOffFullyUsed(DAY_OFF_COUNT, countedDayOff)) {
            throw new IllegalArgumentException("이미 15개의 연차를 모두 사용하였습니다.");
        }

        // 연차 등록
        if (isDayOffRegistrationAllowed(request.getDate(), registrationDate, daysBetween) && !isDayOffExists(employee, request.getDate())) {
            saveDayOff(request.getDate(), employee);
        } else {
            throw new IllegalArgumentException("연차 등록 조건을 만족하지 않습니다.");
        }
    }

    @Transactional
    public int countDayOff(Long id) {

        // 직원 조회
        Employee employee = getEmployeeById(id);

        // 남은 연차 수 확인
        long countedDayOff = countRemainingDayOff(employee);

        if (isFreshMan(employee)) {
            return (int) (FRESHMAN_DAY_OFF_COUNT - countedDayOff);
        } else {
            return (int) (DAY_OFF_COUNT - countedDayOff);
        }
    }

    private boolean isDayOffExists(Employee employee, LocalDate date) {
        return dayOffRepository.existsDayOffByEmployeeAndDate(employee, date);
    }

    private boolean isDayOffRegistrationAllowed(LocalDate requestDate, int registrationDate, long daysBetween) {
        return daysBetween >= registrationDate;
    }

    private boolean isDayOffFullyUsed(long totalDayOffCount, long countedDayOff) {
        return totalDayOffCount <= countedDayOff;
    }

    private boolean isFreshMan(Employee employee) {
        return employee.getWorkStartDate().getYear() == LocalDate.now().getYear();
    }

    private int getRegistrationDate(Employee employee) {
        return employee.getTeam().getDayOffRule().getRegistrationDate();
    }

    private long countRemainingDayOff(Employee employee) {
        return dayOffRepository.countDayOffByEmployee(employee);
    }

    private long calculateDaysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    private void saveDayOff(LocalDate date, Employee employee) {
        DayOff dayOff = new DayOff(date, employee);
        dayOffRepository.save(dayOff);
    }

    private Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findEmployeeById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다."));
    }
}
