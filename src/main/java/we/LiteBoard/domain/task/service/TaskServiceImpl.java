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
import we.LiteBoard.domain.todo.dto.TodoResponseDTO;
import we.LiteBoard.domain.todo.entity.Todo;
import we.LiteBoard.global.exception.CustomException;
import we.LiteBoard.global.exception.ErrorCode;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRepository memberRepository;

    /**
     * 업무 생성
     * @param categoryId 생성할 업무가 속할 카테고리의 ID
     * @param request 생성 정보
     * @return 생성된 업무의 ID
     */
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

    /**
     * 카테고리에 속한 업무 리스트 조회
     * @param categoryId 조회할 카테고리 ID
     * @return 조회된 업무 리스트
     */
    @Override
    public List<TaskResponseDTO.Detail> getAllByCategoryId(Long categoryId) {
        List<Task> tasks = taskRepository.findAllByCategoryId(categoryId);
        return tasks.stream()
                .map(TaskResponseDTO.Detail::from)
                .collect(Collectors.toList());
    }

    /**
     * 업무 단건 조회
     * @param taskId 조회할 업무 ID
     * @return 조회된 업무 상세 정보
     */
    @Override
    public TaskResponseDTO.Detail getById(Long taskId) {
        Task task = taskRepository.getById(taskId);
        return TaskResponseDTO.Detail.from(task);
    }

    /**
     * 업무 수정
     * @param taskId 수정할 업무 ID
     * @param request 수정 내용
     * @return 수정된 업무 ID
     */
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

    /**
     * 업무 삭제
     * @param taskId 삭제할 업무 ID
     */
    @Override
    @Transactional
    public void deleteById(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    /**
     * 내 업무 조회
     * @param member 요청 보내는 멤버
     * @return 진행 중인 내 업무 정보 반환
     */
    @Override
    public TaskResponseDTO.MyTasksResponse getMyInProgressTasks(Member member) {
        List<Task> tasks = taskRepository.findByMemberAndStatus(member, Status.IN_PROGRESS);

        int total = 0;
        int completed = 0;

        List<TaskResponseDTO.MyTask> myTasks = new ArrayList<>();

        for (Task task : tasks) {
            int taskTotal = task.getTodos().size();
            int taskCompleted = (int) task.getTodos().stream().filter(Todo::isDone).count();

            total += taskTotal;
            completed += taskCompleted;

            long daysLeft = 0;
            if (task.getEndDate() != null) {
                daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), task.getEndDate());
            }

            List<TodoResponseDTO.Detail> todos = task.getTodos().stream()
                    .map(TodoResponseDTO.Detail::from).toList();

            myTasks.add(new TaskResponseDTO.MyTask(
                    task.getId(),
                    task.getTitle(),
                    taskTotal,
                    taskCompleted,
                    daysLeft,
                    task.getStatus().name(),
                    todos
            ));
        }

        return new TaskResponseDTO.MyTasksResponse(
                total,
                completed,
                total - completed,
                myTasks
        );
    }
}
