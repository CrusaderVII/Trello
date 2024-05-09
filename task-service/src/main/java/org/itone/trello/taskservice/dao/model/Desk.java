package org.itone.trello.taskservice.dao.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.itone.trello.taskservice.dto.DeskDTO;
import org.itone.trello.taskservice.dto.creation.DeskCreationDTO;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "desks")
@Data
@NoArgsConstructor
public class Desk {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, name = "desk_id", updatable = false)
    private UUID id;
    @Column(nullable = false, name = "desk_name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "FK_project_id", nullable = false)
    private Project project;
    @OneToMany(mappedBy = "desk", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Board> boards;

    public Desk(String name) {
        this.name = name;
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

    public DeskDTO toDTO() {
        return new DeskDTO(this.id, this.name, this.project.getName());
    }
    public static Desk fromCreationDTO(DeskCreationDTO deskCreationDTO) {
        return new Desk(deskCreationDTO.name());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Desk desk = (Desk) o;
        return Objects.equals(id, desk.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Desk{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
