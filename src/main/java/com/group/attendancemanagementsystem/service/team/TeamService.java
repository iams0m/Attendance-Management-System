package com.group.attendancemanagementsystem.service.team;

import com.group.attendancemanagementsystem.domain.team.Team;
import com.group.attendancemanagementsystem.dto.team.request.RegisterTeamRequest;
import com.group.attendancemanagementsystem.dto.team.response.TeamListResponse;
import com.group.attendancemanagementsystem.repository.team.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public void registerTeam(RegisterTeamRequest request) {

        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("팀 이름은 필수입니다.");
        }

        teamRepository.save(Team.builder().name(request.getName()).build());
    }

    @Transactional
    public List<TeamListResponse> getTeamList() {
        return teamRepository.findAll().stream()
                .map(team -> new TeamListResponse(team.getName(), team.getManager(), team.getMemberCount()))
                        .collect(Collectors.toList());

    }
}
