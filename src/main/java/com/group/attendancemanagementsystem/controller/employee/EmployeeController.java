package com.group.attendancemanagementsystem.controller.employee;

import com.group.attendancemanagementsystem.dto.employee.request.RegisterEmployeeRequest;
import com.group.attendancemanagementsystem.service.employee.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/employee")
    public void registerEmployee(@RequestBody RegisterEmployeeRequest request) {
        employeeService.registerEmployee(request);
    }
}
