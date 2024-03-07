package com.group.attendancemanagementsystem.dto.team.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TeamListResponse {

    private String name;
    private String manager;
    private int memberCount;

}
