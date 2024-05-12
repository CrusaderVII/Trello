package org.itone.trello.taskservice.dao.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.itone.trello.taskservice.dto.BoardDTO;
import org.itone.trello.taskservice.dto.creation.BoardCreationDTO;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "boards")
@Data
@NoArgsConstructor
public class Board {

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

    public Board(String name) {
        this.name = name;
    }

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
        return new BoardDTO(this.id, this.name, this.desk.getId());
    }
    public static Board fromCreationDTO(BoardCreationDTO boardCreationDTO) {
        return new Board(boardCreationDTO.name());
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
                ", desk=" + desk.getName() +
                '}';
    }
}
