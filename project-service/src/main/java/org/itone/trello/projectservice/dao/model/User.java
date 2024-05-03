package org.itone.trello.projectservice.dao.model;

import jakarta.persistence.*;
import lombok.Data;
import org.itone.trello.projectservice.dto.UserDTO;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, name = "user_id", updatable = false)
    private UUID id;

    @Column(name = "user_name", nullable = false)
    private String name;

    //TODO: study sql indexes and implement them
    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Column(name = "user_password", nullable = false)
    private String password;
    @ManyToMany(mappedBy = "users")
    private Set<Project> projects;
    @ManyToMany(mappedBy = "users")
    private Set<Task> tasks;

    public void addProject(Project project) {
        if (this.projects == null) this.projects = new HashSet<>();
        this.projects.add(project);

        if (project.getUsers() == null) project.setUsers(new HashSet<>());
        project.getUsers().add(this);
    }

    public void removeProject(UUID id) {
        Project project = projects.stream()
                .filter(currentProject -> currentProject.getId().equals(id))
                .findFirst()
                .orElse(null);

        if(project != null) {
            this.projects.remove(project);
        }
    }

    public UserDTO toDTO () {
        return new UserDTO(this.id, this.name, this.email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
