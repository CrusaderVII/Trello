package org.itone.trello.projectservice.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "desks")
public class Desk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "desk_id", updatable = false)
    private long id;
    @Column(nullable = false, name = "desk_name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "FK_project_id", nullable = false)
    private Project project;
    @OneToMany(mappedBy = "desk")
    private Set<Board> boards;
}
