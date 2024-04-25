package org.itone.trello.projectservice.repository;

import org.itone.trello.projectservice.model.Desk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeskRepository extends JpaRepository<Desk, Long> {
}
