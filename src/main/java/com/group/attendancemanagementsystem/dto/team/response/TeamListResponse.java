package com.group.attendancemanagementsystem.dto.team.response;

public class TeamListResponse {

    private String name;
    private String manager;
    private Long memberCount;

    protected TeamListResponse() {
    }

    public TeamListResponse(String name, String manager, Long memberCount) {
        this.name = name;
        this.manager = manager;
        this.memberCount = memberCount;
    }

    public String getName() {
        return name;
    }

    public String getManager() {
        return manager;
    }

    public Long getMemberCount() {
        return memberCount;
    }
}
