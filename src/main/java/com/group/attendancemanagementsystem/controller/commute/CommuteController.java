package com.group.attendancemanagementsystem.controller.commute;

import com.group.attendancemanagementsystem.dto.commute.request.CommuteByAllDayOfMonthRequest;
import com.group.attendancemanagementsystem.dto.commute.response.CommuteResponse;
import com.group.attendancemanagementsystem.dto.commute.response.OvertimeResponse;
import com.group.attendancemanagementsystem.service.commute.CommuteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommuteController {
    private final CommuteService commuteService;

    public CommuteController(CommuteService commuteService) {
        this.commuteService = commuteService;
    }

    @PostMapping("/commute/start")
    public void startedCommute(@RequestParam(name = "employeeId") Long employeeId) {
        commuteService.startedCommute(employeeId);
    }

    @PostMapping("/commute/end")
    public void endedCommute(@RequestParam(name = "employeeId") Long employeeId) {
        commuteService.endedCommute(employeeId);
    }

    @GetMapping("/commute")
    public CommuteResponse employeeCommuteByAllDayOfMonth(@RequestParam(name = "id") Long id, @RequestParam(name = "date") String date) {
        CommuteByAllDayOfMonthRequest request = new CommuteByAllDayOfMonthRequest(id, date);
        return commuteService.getEmployeeMonthlyWorkHours(request);
    }

    @GetMapping("/overtime")
    public List<OvertimeResponse> employeeOvertimeList(@RequestParam(name = "date") String date) {
        return commuteService.employeeOvertimeList(date);
    }
}
