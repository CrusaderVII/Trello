package org.itone.trello.projectservice.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    //TODO: validate email
    @Column(name = "email", nullable = false)
    private String email;

    //TODO: validate password
    //TODO: store encrypted password
    @Column(name = "password", nullable = false)
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

    public String getLogin() {
        return name;
    }

    public void setLogin(String name) {
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

    @Override
    public String toString() {
        return "User{" +
                "login='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
