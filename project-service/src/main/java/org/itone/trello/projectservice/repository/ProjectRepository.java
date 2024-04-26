package org.itone.trello.projectservice.repository;

import org.itone.trello.projectservice.model.Project;
import org.itone.trello.projectservice.model.User;
import org.itone.trello.projectservice.model.Desk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(value = """
            select users
            from Project p 
            join p.users users
            where p.id = ?1""")
    List<User> findAllUsersOnProject(long projectId);

    @Query(value = """
            select desks 
            from Project p
            join p.desks desks
            where p.id = ?1""")
    List<Desk> findAllDesksOnProject(long projectId);
}
