package org.itone.trello.projectservice.dao.model;

import jakarta.persistence.*;
import lombok.Data;
import org.itone.trello.projectservice.dto.BoardDTO;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "boards")
@Data
public class Board {

    //TODO: implement serializable interface to model
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, name = "board_id", updatable = false)
    private UUID id;
    @Column(nullable = false, name = "board_name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "FK_desk_id", nullable = false)
    private Desk desk;
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Task> tasks;

    public void addTask(Task task) {
        //If it is 1st task we should create a new HashSet before adding new task
        if (this.tasks == null) this.tasks = new HashSet<>();
        this.tasks.add(task);

        //Set to added task this project
        task.setBoard(this);
    }

    public void removeTask(Task task) {
        if (tasks == null) return;
        tasks.remove(task);
    }

    public BoardDTO toDTO() {
        return new BoardDTO(this.id, this.name, this.desk.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Objects.equals(id, board.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desk=" + desk +
                '}';
    }
}
