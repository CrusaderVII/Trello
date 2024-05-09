package org.itone.trello.taskservice.dao.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.itone.trello.taskservice.dto.ProjectDTO;
import org.itone.trello.taskservice.dto.creation.ProjectCreationDTO;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, name = "project_id", updatable = false)
    private UUID id;
    @Column(nullable = false, name = "project_name")
    private String name;
    @Column(name = "project_description", length = 500)
    private String description;

    //I chose Set instead of List, because, as I've read, Hibernate works with Lists inefficiently
    @ManyToMany
    @JoinTable(name = "projects_and_users",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    Set<User> users;
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    Set<Desk> desks;

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void addUser(User user) {
        //If it is 1st user we should create a new HashSet before adding new user
        if (this.users == null) this.users = new HashSet<>();
        this.users.add(user);

        //If it is 1st project of a user we also should create a new HashSet before adding this project
        if (user.getProjects() == null) user.setProjects(new HashSet<>());
        user.getProjects().add(this);
    }

    public void removeUser(UUID id) {
        //Check set of user contains user with specified id
        User user = users.stream()
                .filter(currentUser -> currentUser.getId().equals(id))
                .findFirst()
                .orElse(null);

        //Only if user present and not null we can remove him from set of users
        if(user != null) {
            this.users.remove(user);
            user.getProjects().remove(this);
        }
    }

    public void addDesk(Desk desk) {
        //If it is 1st desk we should create a new HashSet before adding new desk
        if (this.desks == null) this.desks = new HashSet<>();
        this.desks.add(desk);

        //Set to added desk this project
        desk.setProject(this);
    }

    public ProjectDTO toDTO () {
        return new ProjectDTO(this.id, this.name, this.description);
    }
    public static Project fromCreationDTO(ProjectCreationDTO projectCreationDTO) {
        return new Project(projectCreationDTO.name(), projectCreationDTO.description());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
