package org.itone.trello.projectservice.repository;

import org.itone.trello.projectservice.model.Board;
import org.itone.trello.projectservice.model.Desk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeskRepository extends JpaRepository<Desk, Long> {

    @Query("""
            select boards
            from Desk d
            join d.boards
            where d.id=?1""")
    public List<Board> findAllBoardsOnDesk(long deskId);
}
