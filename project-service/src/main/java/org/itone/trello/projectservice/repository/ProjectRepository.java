package org.itone.trello.projectservice.repository;

import org.hibernate.annotations.NamedNativeQuery;
import org.itone.trello.projectservice.model.Project;
import org.itone.trello.projectservice.model.User;
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
            where p.id = ?1
            """)
    List<User> findAllUsersOnProject(long projectId);
}
