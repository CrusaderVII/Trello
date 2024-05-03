package org.itone.trello.projectservice.dao.model;

import jakarta.persistence.*;
import lombok.Data;
import org.itone.trello.projectservice.dto.TaskDTO;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, name = "task_id", updatable = false)
    private UUID id;
    @Column(nullable = false, name = "task_name")
    private String name;
    @Column(nullable = false, name = "task_description", length = 500)
    private String description;
    @ManyToMany
    @JoinTable(name = "tasks_and_users",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;
    @ManyToOne
    @JoinColumn(name = "FK_board_id", nullable = false)
    private Board board;

    public void addUser(User user) {
        if (this.users == null) this.users = new HashSet<>();
        this.users.add(user);

        if (user.getTasks() == null) user.setTasks(new HashSet<>());
        user.getTasks().add(this);
    }

    public void removeUser(UUID id) {
        User user = users.stream()
                .filter(currentUser -> currentUser.getId().equals(id))
                .findFirst()
                .orElse(null);

        if(user != null) {
            this.users.remove(user);
            user.getTasks().remove(this);
        }
    }

    public TaskDTO toDTO () {
        return new TaskDTO(this.id, this.name, this.description, this.board.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
