package com.group.attendancemanagementsystem.controller.commute;

import com.group.attendancemanagementsystem.service.commute.CommuteService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
