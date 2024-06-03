package com.group.attendancemanagementsystem.dto.commute.response;

public class OvertimeResponse {

    private Long id;
    private String name;
    private int overtimeMinutes;

    protected OvertimeResponse() {
    }

    public OvertimeResponse(Long id, String name, int overtimeMinutes) {
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

    public int getOvertimeMinutes() {
        return overtimeMinutes;
    }
}
