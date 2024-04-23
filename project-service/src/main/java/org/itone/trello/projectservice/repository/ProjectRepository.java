package org.itone.trello.projectservice.repository;

import org.itone.trello.projectservice.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
