package we.LiteBoard.domain.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import we.LiteBoard.domain.category.entity.Category;
import we.LiteBoard.domain.category.repository.CategoryRepository;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.member.repository.MemberRepository;
import we.LiteBoard.domain.task.dto.TaskRequestDTO;
import we.LiteBoard.domain.task.dto.TaskResponseDTO;
import we.LiteBoard.domain.task.entity.Task;
import we.LiteBoard.domain.task.enumerate.Status;
import we.LiteBoard.domain.task.repository.TaskRepository;
import we.LiteBoard.global.exception.CustomException;
import we.LiteBoard.global.exception.ErrorCode;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public TaskResponseDTO.Upsert create(Long categoryId, TaskRequestDTO.Create request) {
        Category category = categoryRepository.getById(categoryId);
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .status(Status.PENDING)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .category(category)
                .member(member)
                .build();

        return TaskResponseDTO.Upsert.from(taskRepository.save(task).getId());
    }

    @Override
    public List<TaskResponseDTO.Detail> getAllByCategoryId(Long categoryId) {
        List<Task> tasks = taskRepository.findAllByCategoryId(categoryId);
        return tasks.stream()
                .map(TaskResponseDTO.Detail::from)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponseDTO.Detail getById(Long taskId) {
        Task task = taskRepository.getById(taskId);
        return TaskResponseDTO.Detail.from(task);
    }

    @Override
    @Transactional
    public TaskResponseDTO.Upsert update(Long taskId, TaskRequestDTO.Update request) {
        Task task = taskRepository.getById(taskId);
        task.update(
                request.title(),
                request.description(),
                request.status(),
                request.startDate(),
                request.endDate()
        );
        return TaskResponseDTO.Upsert.from(task.getId());
    }

    @Override
    @Transactional
    public void deleteById(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
