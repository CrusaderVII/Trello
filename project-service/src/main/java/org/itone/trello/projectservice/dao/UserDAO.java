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

//I chose @Repository annotation for DAO, because Spring manual said this. I also moved all transactions to DAO layer,
//because in using @Transactional on services has a bad effect on performance (with time and growth of application
//adding more business logic to services will have more bad impact on performance).
//And I don't use try/catch blocks, because than transactions won't roll back due to work mechanism of
//@Transactional annotation.
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
