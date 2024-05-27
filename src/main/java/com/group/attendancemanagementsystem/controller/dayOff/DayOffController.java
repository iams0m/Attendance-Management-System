package com.group.attendancemanagementsystem.controller.dayOff;

import com.group.attendancemanagementsystem.dto.dayOff.request.RegisterDayOffRequest;
import com.group.attendancemanagementsystem.service.dayOff.DayOffService;
import org.springframework.web.bind.annotation.*;

@RestController
public class DayOffController {

    private final DayOffService dayOffService;

    public DayOffController(DayOffService dayOffService) {
        this.dayOffService = dayOffService;
    }

    @PostMapping("/dayoff")
    public void registerDayOff(@RequestBody RegisterDayOffRequest request) {
        dayOffService.registerDayOff(request);
    }

    @GetMapping("/dayoff")
    public int countDayOff(@RequestParam(name = "id") Long id) {
        return dayOffService.countDayOff(id);
    }
}
