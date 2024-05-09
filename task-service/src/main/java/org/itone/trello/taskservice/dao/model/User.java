package org.itone.trello.taskservice.dao.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.itone.trello.taskservice.dto.UserDTO;
import org.itone.trello.taskservice.dto.creation.UserCreationDTO;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, name = "user_id", updatable = false)
    private UUID id;

    @Column(name = "user_name", nullable = false)
    private String name;

    //Don't use SQL index on this field, because it is effective when our DB is big and data is saved to DB on plan.
    //But here that is not the case.
    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Column(name = "user_password", nullable = false)
    private String password;
    @ManyToMany(mappedBy = "users")
    private Set<Project> projects;
    @ManyToMany(mappedBy = "users")
    private Set<Task> tasks;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

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

    public static User fromUserCreationDTO(UserCreationDTO userCreationDTO) {
        return new User(userCreationDTO.name(), userCreationDTO.email(), userCreationDTO.password());
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
