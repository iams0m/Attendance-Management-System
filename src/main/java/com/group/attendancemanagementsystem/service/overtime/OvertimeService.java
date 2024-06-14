package com.group.attendancemanagementsystem.service.overtime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.attendancemanagementsystem.domain.commute.Commute;
import com.group.attendancemanagementsystem.domain.employee.Employee;
import com.group.attendancemanagementsystem.dto.overtime.response.OvertimeResponse;
import com.group.attendancemanagementsystem.repository.employee.EmployeeRepository;
import com.group.attendancemanagementsystem.repository.overtime.OvertimeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class OvertimeService {
    private final EmployeeRepository employeeRepository;
    private final OvertimeRepository overtimeRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${api.secret.key}") // application.yml에 저장된 키 값 가져오기
    private String key;

    public OvertimeService(EmployeeRepository employeeRepository, OvertimeRepository overtimeRepository, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.employeeRepository = employeeRepository;
        this.overtimeRepository = overtimeRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<OvertimeResponse> calculateOvertimeHours(YearMonth date) {

        // 년-월 분석 및 해당 월의 총 근무일 계산 (공휴일 제외)
        int totalWorkingDays = calculateWorkingDays(date);

        // 근로 계약 기준 표준 근무 시간 (분 단위)
        int standardWorkingHours = totalWorkingDays * 8;
        int standardWorkingMinutes = standardWorkingHours * 60; // 시간 -> 분으로 환산
        System.out.println("standardWorkingMinutes = " + standardWorkingMinutes);

        List<OvertimeResponse> results = new ArrayList<>();

        List<Employee> employees = this.employeeRepository.findAll();

        for (Employee employee : employees) {

            // 직원별 총 근무시간 계산 (분 단위)
            int totalWorkMinutes = calculateTotalWorkMinutesForEmployee(employee, date);

            System.out.println("totalWorkMinutes = " + totalWorkMinutes);

            // 초과 근무 계산
            int overtimeMinutes = totalWorkMinutes - standardWorkingMinutes;

            if (overtimeMinutes < 0) {
                overtimeMinutes = 0;
            }

            // 결과 리스트 추가
            results.add(new OvertimeResponse(employee.getId(), employee.getName(), overtimeMinutes));
        }
        return results;
    }

    private int calculateWorkingDays(YearMonth yearMonth) {

        try {
            UriComponents uri = UriComponentsBuilder.newInstance()
                    .scheme("http")
                    .host("apis.data.go.kr")
                    .path("/B090041/openapi/service/SpcdeInfoService/getRestDeInfo")
                    .queryParam("serviceKey", URLEncoder.encode("key", "UTF-8"))
                    .queryParam("solYear", URLEncoder.encode(Integer.toString(yearMonth.getYear()), "UTF-8"))
                    .queryParam("solMonth", URLEncoder.encode(String.format("%02d", yearMonth.getMonthValue()), "UTF-8"))
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", MediaType.APPLICATION_JSON.toString());
            headers.set("Accept", MediaType.APPLICATION_JSON.toString());

            HttpEntity<?> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = this.restTemplate.exchange(uri.toString(), HttpMethod.GET, request, String.class);

            System.out.println(uri.toString());
            System.out.println(response.getBody());

            int totalHoliday = this.objectMapper.readTree(response.getBody()).path("response").path("body").path("totalCount").asInt();
            int weekends = calculateWeekends(yearMonth);
            System.out.println("uri = " + uri);
            System.out.println("yearMonth.lengthOfMonth = " + yearMonth.lengthOfMonth());
            System.out.println("totalHoliday = " + totalHoliday);
            System.out.println("yearMonth.lengthOfMonth - totalHoliday - weekends = " + (yearMonth.lengthOfMonth() - totalHoliday - weekends));

            return Math.max(yearMonth.lengthOfMonth() - totalHoliday - weekends, 0);

        } catch (Exception e) {
            return -1;
        }
    }

    private int calculateTotalWorkMinutesForEmployee(Employee employee, YearMonth yearMonth) {
        List<Commute> commutes = this.overtimeRepository.findByEmployeeIdAndMonthAndYear(employee.getId(), yearMonth.getYear(), yearMonth.getMonthValue());

        long totalWorkingMinutes = commutes.stream()
                .filter(c -> c.getStartedAt() != null && c.getEndedAt() != null)
                .mapToLong(c -> ChronoUnit.MINUTES.between(c.getStartedAt(), c.getEndedAt()))
                .sum();

        return (int) totalWorkingMinutes;
    }

    private int calculateWeekends(YearMonth yearMonth) {
        int weekends = 0;
        LocalDate date = yearMonth.atDay(1);

        while (date.getMonth() == yearMonth.getMonth()) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                weekends++;
            }
            date = date.plusDays(1);
        }

        return weekends;
    }
}
