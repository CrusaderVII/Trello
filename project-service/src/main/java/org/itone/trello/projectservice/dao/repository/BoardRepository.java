package org.itone.trello.projectservice.dao.repository;

import org.itone.trello.projectservice.dao.model.Board;
import org.itone.trello.projectservice.dao.model.Desk;
import org.itone.trello.projectservice.dao.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query(value = """
            select desk 
            from Board b
            join b.desk
            where b.id=?1""")
    public Desk findDeskOfBoard(long boardId);

    @Query(value = """
            select tasks
            from Board b
            join b.tasks
            where b.id=?1""")
    public List<Task> findAllTasksOnBoard(long boardId);
}
