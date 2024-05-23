package com.group.attendancemanagementsystem.dto.dayOff.request;

import java.time.LocalDate;

public class RegisterDayOffRequest {

    private Long id;

    private LocalDate date;

    protected RegisterDayOffRequest() {
    }

    public RegisterDayOffRequest(Long id, LocalDate date) {
        this.id = id;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }
}

