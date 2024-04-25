package org.itone.trello.projectservice.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "project_id", updatable = false)
    private long id;
    @Column(nullable = false, name = "project_name")
    private String name;
    @Column(name = "project_description", length = 500)
    private String description;

    @ManyToMany
    @JoinTable(name = "projects_and_users",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    Set<User> users;
    @OneToMany(mappedBy = "project")
    Set<Desk> desks;

    public Project(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Project() {
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

    public void addUser(User user) {
        if (this.users == null) this.users = new HashSet<>();
        this.users.add(user);

        if (user.getProjects() == null) user.setProjects(new HashSet<>());
        user.getProjects().add(this);
    }

    public void removeUser(long id) {
        User user = users.stream()
                .filter(currentUser -> currentUser.getId()==id)
                .findFirst()
                .orElse(null);

        if(user != null) {
            this.users.remove(user);
            user.getProjects().remove(this);
        }
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
