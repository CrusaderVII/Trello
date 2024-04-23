package org.itone.trello.projectservice.repository;

import org.itone.trello.projectservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
