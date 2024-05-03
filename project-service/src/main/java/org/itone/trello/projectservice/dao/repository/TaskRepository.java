package org.itone.trello.projectservice.dao.repository;

import org.itone.trello.projectservice.dao.model.Task;
import org.itone.trello.projectservice.dao.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

}
