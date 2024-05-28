package com.group.attendancemanagementsystem.service.commute;

import com.group.attendancemanagementsystem.domain.commute.Commute;
import com.group.attendancemanagementsystem.domain.dayOff.DayOff;
import com.group.attendancemanagementsystem.domain.employee.Employee;
import com.group.attendancemanagementsystem.dto.commute.request.CommuteByAllDayOfMonthRequest;
import com.group.attendancemanagementsystem.dto.commute.response.CommuteResponse;
import com.group.attendancemanagementsystem.dto.commute.response.DetailResponse;
import com.group.attendancemanagementsystem.repository.commute.CommuteRepository;
import com.group.attendancemanagementsystem.repository.dayOff.DayOffRepository;
import com.group.attendancemanagementsystem.repository.employee.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommuteService {
    private final CommuteRepository commuteRepository;
    private final EmployeeRepository employeeRepository;
    private final DayOffRepository dayOffRepository;

    public CommuteService(CommuteRepository commuteRepository, EmployeeRepository employeeRepository, DayOffRepository dayOffRepository) {
        this.commuteRepository = commuteRepository;
        this.employeeRepository = employeeRepository;
        this.dayOffRepository = dayOffRepository;
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

    @Transactional
    public void endedCommute(Long employeeId) {

        // 직원 조회
        Employee employee = getEmployeeById(employeeId);

        // 당일 출근 기록이 없는 경우
        Commute commute = commuteRepository.findCommuteByEmployeeAndDate(employee, LocalDate.now())
                .orElseThrow(() -> new IllegalArgumentException("출근 기록이 없습니다."));

        // 출근 기록이 있는 경우 퇴근 처리
        commute.endCommute(LocalTime.now());
        commuteRepository.save(commute);
    }

    @Transactional(readOnly = true)
    public CommuteResponse getEmployeeMonthlyWorkHours(CommuteByAllDayOfMonthRequest request) {

        // 직원 조회
        Employee employee = getEmployeeById(request.getId());

        // 입력 받은 날짜 치환
        String yearMonthString = request.getDate();
        LocalDate startWithOfMonth = YearMonth.parse(yearMonthString).atDay(1);
        LocalDate endWithOfMonth = YearMonth.parse(yearMonthString).atEndOfMonth();

        // 해당 월의 직원 근무 기록 가져오기
        List<Commute> commuteList = commuteRepository
                .findCommutesByEmployeeIdAndDateBetween(employee.getId(), startWithOfMonth, endWithOfMonth);

        // 해당 월의 직원 연차 기록 가져오기
        List<DayOff> dayOffList = dayOffRepository
                .findDaysOffByEmployeeIdAndDateBetween(employee.getId(), startWithOfMonth, endWithOfMonth);

        // detail 안의 날짜, 근무 시간 분으로 변환
        List<DetailResponse> detailResponse = new ArrayList<>();

        LocalDate currentDate = startWithOfMonth;

        while (!currentDate.isAfter(endWithOfMonth)) {
            LocalDate finalCurrentDate = currentDate; // 새로운 변수에 할당

            boolean usingDayOff = dayOffList.stream()
                    .anyMatch(dayOff -> dayOff.getDate().equals(finalCurrentDate));

            if (usingDayOff) {
                // 연차를 사용한 날짜일 경우, 근무 시간을 0으로 설정
                detailResponse.add(new DetailResponse(currentDate, 0L, true));
            } else {
                // 연차를 사용하지 않은 날짜일 경우, 해당 일자의 근무 시간 계산
                long workingMinutes = calculateWorkingMinutes(commuteList, currentDate);
                detailResponse.add(new DetailResponse(currentDate, workingMinutes, false));
            }
            currentDate = currentDate.plusDays(1);
        }

        // 해당 달의 모든 근무 시간 합하기
        long sum = detailResponse.stream()
                .mapToLong(DetailResponse::getWorkingMinutes)
                .sum();

        return new CommuteResponse(detailResponse, sum);
    }

    private Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findEmployeeById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 직원입니다."));
    }

    private long calculateWorkingMinutes(List<Commute> commuteList, LocalDate date) {
        return commuteList.stream()
                .filter(commute -> commute.getDate().equals(date))
                .mapToLong(commute -> Duration.between(commute.getStartedAt(), commute.getEndedAt()).toMinutes())
                .sum();
    } 
}
