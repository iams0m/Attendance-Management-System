package com.group.attendancemanagementsystem.dto.commute.response;

public class OvertimeResponse {

    private Long id;
    private String name;
    private Long overtimeMinutes;

    protected OvertimeResponse() {
    }

    public OvertimeResponse(Long id, String name, Long overtimeMinutes) {
        this.id = id;
        this.name = name;
        this.overtimeMinutes = overtimeMinutes;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getOvertimeMinutes() {
        return overtimeMinutes;
    }
}
