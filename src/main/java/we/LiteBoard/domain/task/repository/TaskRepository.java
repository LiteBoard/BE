package we.LiteBoard.domain.task.repository;

import we.LiteBoard.domain.task.entity.Task;

import java.util.List;

public interface TaskRepository {
    Task save(Task task);
    List<Task> findAllByCategoryId(Long categoryId);
    Task getById(Long taskId);
    void deleteById(Long taskId);
}
