package com.group.attendancemanagementsystem.controller.dayOff;

import com.group.attendancemanagementsystem.dto.dayOff.request.RegisterDayOffRequest;
import com.group.attendancemanagementsystem.service.dayOff.DayOffService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DayOffController {

    private final DayOffService dayOffService;

    public DayOffController(DayOffService dayOffService) {
        this.dayOffService = dayOffService;
    }

    @PostMapping("/dayoff")
    public void registerDayOff(@RequestParam RegisterDayOffRequest request) {
        dayOffService.registerDayOff(request);
    }
}
