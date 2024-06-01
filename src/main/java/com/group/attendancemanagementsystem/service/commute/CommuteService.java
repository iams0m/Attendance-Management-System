package com.group.attendancemanagementsystem.service.commute;

import com.group.attendancemanagementsystem.api.ApiExplorer;
import com.group.attendancemanagementsystem.domain.commute.Commute;
import com.group.attendancemanagementsystem.domain.dayOff.DayOff;
import com.group.attendancemanagementsystem.domain.employee.Employee;
import com.group.attendancemanagementsystem.dto.commute.request.CommuteByAllDayOfMonthRequest;
import com.group.attendancemanagementsystem.dto.commute.response.CommuteResponse;
import com.group.attendancemanagementsystem.dto.commute.response.DetailResponse;
import com.group.attendancemanagementsystem.dto.commute.response.OvertimeResponse;
import com.group.attendancemanagementsystem.repository.commute.CommuteRepository;
import com.group.attendancemanagementsystem.repository.dayOff.DayOffRepository;
import com.group.attendancemanagementsystem.repository.employee.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

    public List<OvertimeResponse> calculateOvertimeHours(String date) {

        // 입력 받은 날짜 치환
        LocalDate startWithOfMonth = YearMonth.parse(date).atDay(1);
        LocalDate endWithOfMonth = YearMonth.parse(date).atEndOfMonth();

        // 해당 월의 전체 일 수 계산
        int daysInMonth = endWithOfMonth.getDayOfMonth();

        // 공휴일 목록 가져오기
        List<String> totalHoliday = null;
        try {
            totalHoliday = ApiExplorer.findHoliday();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<LocalDate> localDates = changeTimeFormat(totalHoliday);

        // 해당 년-월의 날짜 개수 세기
        int count = countDatesStartingWithPrefix(localDates, date);

        // 주말 + 공휴일 개수 계산
        long weekends = countWeekends(startWithOfMonth, endWithOfMonth);
        count += (int) weekends;

        // 근로 계약 기준 표준 근무 시간
        int standardWorkingHours = (daysInMonth - count) * 8;
        int standardWorkingMinutes = standardWorkingHours * 60; // 시간 -> 분으로 환산

        // 근무 기록이 존재하는 직원 id 목록 가져오기
        List<Long> employeeId = commuteRepository.findByEmployeeId();

        LocalDate currentDate = startWithOfMonth;

        List<OvertimeResponse> results = new ArrayList<>();

        for (Long id : employeeId) {

            // 해당 월의 직원 근무 기록 가져오기
            List<Commute> commuteList = commuteRepository
                    .findCommutesByEmployeeIdAndDateBetween(id, startWithOfMonth, endWithOfMonth);

            // 근무 시간 계산
            long workingMinutes = 0;

            while (currentDate.isAfter(endWithOfMonth)) {
                workingMinutes += calculateWorkingMinutes(commuteList, currentDate);
                currentDate = currentDate.plusDays(1);
            }

            // 초과 근무 시간 계산
            long overtimes = 0;
            if (workingMinutes > standardWorkingMinutes) {
                overtimes = workingMinutes - standardWorkingMinutes;

                System.out.println("overtimes = " + overtimes);
                System.out.println("standardWorkingMinutes = " + standardWorkingMinutes);
            }

            System.out.println("overtimes = " + overtimes);
            System.out.println("standardWorkingMinutes = " + standardWorkingMinutes);

            String name = employeeRepository.findNameById(id);

            results.add(new OvertimeResponse(id, name, overtimes));
            }
            return results;
        }

    private long countWeekends(LocalDate starDate, LocalDate endDate) {
        long daysBetween = ChronoUnit.DAYS.between(starDate, endDate);
        long count = 0;

        for (int i = 0; i <= daysBetween; i++) {
            LocalDate currentDate = starDate.plusDays(i);
            if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                count++;
            }
        }

        return count;
    }

    private List<LocalDate> changeTimeFormat(List<String> stringHolidayLists) {
        List<LocalDate> dates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        for (String dateString : stringHolidayLists) {
            LocalDate date = LocalDate.parse(dateString, formatter);
            dates.add(date);
        }
        return dates;
    }

    private static int countDatesStartingWithPrefix(List<LocalDate> dates, String datePrefix) {
        int count = 0;
        for (LocalDate date : dates) {
            if (date.toString().startsWith(datePrefix)) {
                count++;
            }
        }
        return count;
    }
}
