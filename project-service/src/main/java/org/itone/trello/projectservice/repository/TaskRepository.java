package org.itone.trello.projectservice.repository;

import org.itone.trello.projectservice.model.Task;
import org.itone.trello.projectservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("""
            select users 
            from Task t
            join t.users users
            where t.id=?1""")
    public List<User> findAllUsersOnTask(long taskId);
}
