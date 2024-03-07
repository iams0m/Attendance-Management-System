package com.group.attendancemanagementsystem.domain.team;

import jakarta.persistence.*;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String manager;

    private Long memberCount;

    protected Team() {
    }

    public Team(String name) {
        this.name = name;
    }
}
