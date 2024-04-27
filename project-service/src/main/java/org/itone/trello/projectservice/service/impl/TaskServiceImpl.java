package org.itone.trello.projectservice.service.impl;

import org.itone.trello.projectservice.exception.task.NoSuchTaskException;
import org.itone.trello.projectservice.model.Board;
import org.itone.trello.projectservice.model.Task;
import org.itone.trello.projectservice.model.User;
import org.itone.trello.projectservice.repository.TaskRepository;
import org.itone.trello.projectservice.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task getTaskById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchTaskException("id "+id));
    }

    @Override
    public Task saveTask(Task entity) {
        return taskRepository.save(entity);
    }

    @Override
    public void deleteTask(long id) {
        taskRepository.deleteById(id);
    }
}
