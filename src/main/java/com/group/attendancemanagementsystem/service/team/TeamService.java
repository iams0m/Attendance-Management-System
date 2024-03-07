package com.group.attendancemanagementsystem.service.team;

import com.group.attendancemanagementsystem.domain.team.Team;
import com.group.attendancemanagementsystem.dto.team.request.RegisterTeamRequest;
import com.group.attendancemanagementsystem.repository.team.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public void registerTeam(RegisterTeamRequest request) {
        teamRepository.save(new Team(request.getName()));
    }
}
