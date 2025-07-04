package we.LiteBoard.domain.task.service;

import we.LiteBoard.domain.task.dto.TaskRequestDTO;
import we.LiteBoard.domain.task.dto.TaskResponseDTO;

import java.util.List;

public interface TaskService {
    TaskResponseDTO.Upsert create(Long categoryId, TaskRequestDTO.Create request);
    List<TaskResponseDTO.Detail> getAllByCategoryId(Long categoryId);
    TaskResponseDTO.Detail getById(Long taskId);
    TaskResponseDTO.Upsert update(Long taskId, TaskRequestDTO.Update request);
    void deleteById(Long taskId);
}
