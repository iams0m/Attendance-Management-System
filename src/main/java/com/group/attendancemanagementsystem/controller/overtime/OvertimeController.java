package com.group.attendancemanagementsystem.controller.overtime;

import com.group.attendancemanagementsystem.dto.overtime.response.OvertimeResponse;
import com.group.attendancemanagementsystem.service.overtime.OvertimeService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;
import java.util.List;

@RestController
public class OvertimeController {
    private final OvertimeService overtimeService;

    public OvertimeController(OvertimeService overtimeService) {
        this.overtimeService = overtimeService;
    }

    @GetMapping("/overtime")
    public List<OvertimeResponse> employeeOvertimeList(@DateTimeFormat(pattern = "yyyy-MM") YearMonth date) {
        return overtimeService.calculateOvertimeHours(date);
    }
}
