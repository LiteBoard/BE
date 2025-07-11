package we.LiteBoard.domain.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import we.LiteBoard.domain.category.entity.Category;
import we.LiteBoard.domain.category.repository.CategoryRepository;
import we.LiteBoard.domain.member.dto.MemberResponseDTO;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.member.repository.MemberRepository;
import we.LiteBoard.domain.notification.service.NotificationServiceImpl;
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
    private final NotificationServiceImpl notificationService;

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

        Task saved = taskRepository.save(task);
        notificationService.notifyTaskAssigned(saved);

        return TaskResponseDTO.Upsert.from(saved.getId());
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
    @Transactional
    public TaskResponseDTO.Detail getById(Long taskId) {
        Task task = taskRepository.getById(taskId);
        task.refreshStatus();

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
    @Transactional // 업무 상태 변환 필요 (지연된 경우)
    public TaskResponseDTO.MyTasksResponse getMyInProgressTasks(Member member) {
        List<Status> targetStatuses = List.of(Status.IN_PROGRESS, Status.DELAYED);
        List<Task> tasks = taskRepository.findByMemberAndStatuses(member, targetStatuses);

        tasks.forEach(Task::refreshStatus);

        List<TaskResponseDTO.MyTask> myTasks = tasks.stream()
                .map(TaskResponseDTO.MyTask::from)
                .toList();

        int total = myTasks.stream().mapToInt(TaskResponseDTO.MyTask::totalTodoCount).sum();
        int completed = myTasks.stream().mapToInt(TaskResponseDTO.MyTask::completedTodoCount).sum();

        MemberResponseDTO.Detail myInfo = MemberResponseDTO.Detail.from(member);

        return new TaskResponseDTO.MyTasksResponse(
                myInfo,
                total,
                completed,
                total - completed,
                myTasks
        );
    }
}
