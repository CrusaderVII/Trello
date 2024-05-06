package org.itone.trello.projectservice.dao;

import jakarta.transaction.Transactional;
import org.itone.trello.projectservice.dao.model.User;
import org.itone.trello.projectservice.dao.repository.UserRepository;
import org.itone.trello.projectservice.util.exception.user.NoSuchUserException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

//I chose @Repository annotation for DAO, because Spring manual said this
@Transactional
@Repository
public class UserDAO {
    private final UserRepository userRepository;

    public UserDAO(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(UUID id) throws NoSuchUserException{
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchUserException("id "+id));
    }

    public User findByEmail(String email) throws NoSuchUserException{
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchUserException("email "+email));
    }

    public List<User> findAll(int page) {
        Pageable pageable = PageRequest.of(page, 20);

        return userRepository.findAllUsers(pageable)
                .stream()
                .toList();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }
}
