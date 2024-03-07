package com.group.attendancemanagementsystem.repository.team;

import com.group.attendancemanagementsystem.domain.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
