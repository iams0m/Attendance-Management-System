package com.group.attendancemanagementsystem.dto.dayOff.request;

import java.time.LocalDate;

public class DayOffRegisterRequest {

    private Long id;

    private LocalDate date;

    protected DayOffRegisterRequest() {
    }

    public DayOffRegisterRequest(Long id, LocalDate date) {
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

