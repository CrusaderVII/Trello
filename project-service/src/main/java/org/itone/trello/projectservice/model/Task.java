package org.itone.trello.projectservice.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "task_id", updatable = false)
    private long id;
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

    public Task(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Task() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void addUser(User user) {
        if (this.users == null) this.users = new HashSet<>();
        this.users.add(user);

        if (user.getTasks() == null) user.setTasks(new HashSet<>());
        user.getTasks().add(this);
    }

    public void removeUser(long id) {
        User user = users.stream()
                .filter(currentUser -> currentUser.getId()==id)
                .findFirst()
                .orElse(null);

        if(user != null) {
            this.users.remove(user);
            user.getTasks().remove(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
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
                ", description='" + description + '\'' +
                '}';
    }
}
