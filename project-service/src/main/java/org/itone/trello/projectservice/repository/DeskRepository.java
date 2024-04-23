package org.itone.trello.projectservice.repository;

import org.itone.trello.projectservice.model.Desk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeskRepository extends JpaRepository<Desk, Long> {
}
