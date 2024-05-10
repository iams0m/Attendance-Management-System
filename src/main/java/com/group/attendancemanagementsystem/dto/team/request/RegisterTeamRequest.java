package com.group.attendancemanagementsystem.dto.team.request;

public class RegisterTeamRequest {

    private String name;
    private String manager;

    protected RegisterTeamRequest() {
    }

    public RegisterTeamRequest(String name) {
        this.name = name;
    }

    public RegisterTeamRequest(String name, String manager) {
        this.name = name;
        this.manager = manager;
    }

    public String getName() {
        return name;
    }

    public String getManager() {
        return manager;
    }
}
