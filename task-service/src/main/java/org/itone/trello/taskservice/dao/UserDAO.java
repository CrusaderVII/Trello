package org.itone.trello.taskservice.dao;

import org.itone.trello.taskservice.dao.model.User;

import java.util.List;
import java.util.UUID;

public interface UserDAO {
    public User findById(UUID id);
    public User findByEmail(String email);
    public List<User> findAll(int page);
    public User save(User user);
    public void deleteById(UUID id);
}
