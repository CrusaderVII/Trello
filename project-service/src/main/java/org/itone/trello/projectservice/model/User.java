package org.itone.trello.projectservice.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "user_id", updatable = false)
    private long id;
    //TODO: validate name
    @Column(name = "user_name", nullable = false)
    private String name;

    //TODO: validate email
    @Column(name = "user_email", nullable = false)
    private String email;

    //TODO: validate password
    //TODO: store encrypted password
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

    public User() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public void addProject(Project project) {
        if (this.projects == null) this.projects = new HashSet<>();
        this.projects.add(project);

        if (project.getUsers() == null) project.setUsers(new HashSet<>());
        project.getUsers().add(this);
    }

    public void removeProject(long id) {
        Project project = projects.stream()
                .filter(currentProject -> currentProject.getId()==id)
                .findFirst()
                .orElse(null);

        if(project != null) {
            this.projects.remove(project);
            project.getUsers().remove(this);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
