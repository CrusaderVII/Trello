package org.itone.trello.projectservice.model;

import jakarta.persistence.*;

import java.util.HashSet;
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

    public Desk(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Desk() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<Board> getBoards() {
        return boards;
    }

    public void setBoards(Set<Board> boards) {
        this.boards = boards;
    }

    public void addBoard(Board board) {
        if (boards == null) boards = new HashSet<>();
        boards.add(board);

        board.setDesk(this);
    }

    public void removeBoard(Board board) {
        if (boards == null) return;
        boards.remove(board);
    }

    @Override
    public String toString() {
        return "Desk{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
