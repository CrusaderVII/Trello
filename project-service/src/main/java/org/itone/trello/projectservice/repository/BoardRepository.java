package org.itone.trello.projectservice.repository;

import org.itone.trello.projectservice.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
