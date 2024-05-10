package com.group.attendancemanagementsystem.repository.team;

import com.group.attendancemanagementsystem.domain.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, String> {
    boolean existsByName(String name);
    Optional<Team> findByName(String name);

}
