package com.group.attendancemanagementsystem.service.team;

import com.group.attendancemanagementsystem.domain.team.Team;
import com.group.attendancemanagementsystem.dto.team.request.RegisterTeamRequest;
import com.group.attendancemanagementsystem.dto.team.response.TeamFindAllResponse;
import com.group.attendancemanagementsystem.repository.team.TeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Transactional
    public void registerTeam(RegisterTeamRequest request) {

        if (teamRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException(String.format("이미 존재하는 팀입니다."));
        }

        Team team = new Team(request.getName());
        teamRepository.save(team);
    }

    public List<TeamFindAllResponse> teamFindAllResponses() {

        List<Team> teamList = teamRepository.findAll();
        return teamList.stream()
                .map(team -> new TeamFindAllResponse(
                        team.getName(),
                        team.getManager(),
                        team.getEmployeeCount()
                )).collect(Collectors.toList());
    }
}
