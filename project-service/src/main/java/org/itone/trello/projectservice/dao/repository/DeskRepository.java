package org.itone.trello.projectservice.dao.repository;

import org.itone.trello.projectservice.dao.model.Board;
import org.itone.trello.projectservice.dao.model.Desk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeskRepository extends JpaRepository<Desk, UUID> {

}
