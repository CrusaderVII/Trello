package org.itone.trello.projectservice.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
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

    //I chose Set instead of List, because, as I've read, Hibernate works with Lists inefficiently
    @ManyToMany
    @JoinTable(name = "projects_and_users",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    Set<User> users;
    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
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

    public Set<Desk> getDesks() {
        return desks;
    }

    public void setDesks(Set<Desk> desks) {
        this.desks = desks;
    }

    public void addUser(User user) {
        //If it is 1st user we should create a new HashSet before adding new user
        if (this.users == null) this.users = new HashSet<>();
        this.users.add(user);

        //If it is 1st project of a user we also should create a new HashSet before adding this project
        if (user.getProjects() == null) user.setProjects(new HashSet<>());
        user.getProjects().add(this);
    }

    public void removeUser(long id) {
        //Check set of user contains user with specified id
        User user = users.stream()
                .filter(currentUser -> currentUser.getId()==id)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id == project.id;
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
                ", description='" + description + '\'' +
                '}';
    }
}
