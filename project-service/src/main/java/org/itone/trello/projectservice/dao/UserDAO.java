package org.itone.trello.projectservice.dao;

import org.itone.trello.projectservice.dao.model.User;
import org.itone.trello.projectservice.util.exception.user.NoSuchUserException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserDAO {
    public User findById(UUID id);
    public User findByEmail(String email);
    public List<User> findAll(int page);
    public User save(User user);
    public void deleteById(UUID id);
}
