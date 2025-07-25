package we.LiteBoard.domain.task.service;

import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.task.dto.TaskRequestDTO;
import we.LiteBoard.domain.task.dto.TaskResponseDTO;

import java.util.List;

public interface TaskService {
    TaskResponseDTO.Upsert create(Long categoryId, TaskRequestDTO.Create request);
    void assignMembers(Long taskId, List<Long> longs, Member currentMember);
    void removeMember(Long taskId, Long memberId);
    List<TaskResponseDTO.Detail> getAllByCategoryId(Long categoryId);
    TaskResponseDTO.Detail getById(Long taskId);
    TaskResponseDTO.Upsert update(Long taskId, TaskRequestDTO.Update request);
    void deleteById(Long taskId);
    TaskResponseDTO.MyTasksResponse getMyInProgressTasks(Member currentMember);
}
