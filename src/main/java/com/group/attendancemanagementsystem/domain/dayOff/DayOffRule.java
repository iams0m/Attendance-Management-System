package com.group.attendancemanagementsystem.domain.dayOff;

import com.group.attendancemanagementsystem.domain.team.Team;
import jakarta.persistence.*;

@Entity
public class DayOffRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day_off_rule_id")
    private Long id;

    @Column(name = "registration_date")
    private int registrationDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    protected DayOffRule() {
    }

    public DayOffRule(int registrationDate, Team team) {
        this.registrationDate = registrationDate;
        this.team = team;
    }

    public Long getId() {
        return id;
    }

    public int getRegistrationDate() {
        return registrationDate;
    }

    public Team getTeam() {
        return team;
    }
}
