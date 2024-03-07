package com.group.attendancemanagementsystem.controller.team;

import com.group.attendancemanagementsystem.dto.team.request.RegisterTeamRequest;
import com.group.attendancemanagementsystem.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/team")
    public void registerTeam(@RequestBody RegisterTeamRequest request) {
        teamService.registerTeam(request);
    }
}
