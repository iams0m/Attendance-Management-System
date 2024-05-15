package com.group.attendancemanagementsystem.controller.team;

import com.group.attendancemanagementsystem.dto.team.request.RegisterTeamRequest;
import com.group.attendancemanagementsystem.dto.team.response.TeamFindAllResponse;
import com.group.attendancemanagementsystem.service.team.TeamService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/team")
    public void registerTeam(@RequestBody RegisterTeamRequest request) {
        teamService.registerTeam(request);
    }

    @GetMapping("/team")
    public List<TeamFindAllResponse> teamFindAllResponses() {
        return teamService.teamFindAllResponses();
    }

}
