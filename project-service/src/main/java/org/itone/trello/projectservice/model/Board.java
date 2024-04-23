package org.itone.trello.projectservice.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "board_id", updatable = false)
    private long id;
    @Column(nullable = false, name = "board_name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "FK_desk_id", nullable = false)
    private Desk desk;
    @OneToMany(mappedBy = "board")
    private Set<Task> tasks;

    public Board(long id, String name, Desk desk) {
        this.id = id;
        this.name = name;
        this.desk = desk;
    }

    public Board() {}

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

    public Desk getDesk() {
        return desk;
    }

    public void setDesk(Desk desk) {
        this.desk = desk;
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
