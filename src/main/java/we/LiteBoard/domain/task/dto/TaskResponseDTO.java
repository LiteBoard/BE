package we.LiteBoard.domain.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import we.LiteBoard.domain.member.dto.MemberResponseDTO;
import we.LiteBoard.domain.requestCard.dto.RequestCardResponseDTO;
import we.LiteBoard.domain.task.entity.Task;
import we.LiteBoard.domain.task.enumerate.Status;
import we.LiteBoard.domain.todo.dto.TodoResponseDTO;
import we.LiteBoard.domain.todo.entity.Todo;
import we.LiteBoard.global.common.annotation.DateFormat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Schema(description = "업무 관련 응답 DTO")
public class TaskResponseDTO {

    @Schema(description = "업무 생성 응답 DTO")
    public record Upsert(
            Long taskId
    ) {
        public static TaskResponseDTO.Upsert from(Long taskId) {
            return new TaskResponseDTO.Upsert(
                    taskId
            );
        }
    }

    @Schema(description = "업무 상세 조회 응답 DTO")
    public record Detail(
            Long id,
            String title,
            String description,
            Status status,
            @DateFormat LocalDate startDate,
            @DateFormat LocalDate endDate,
            List<MemberResponseDTO.Detail> members,
            int completedTodoCount,
            int pendingTodoCount,
            List<TodoResponseDTO.Detail> todos,
            List<RequestCardResponseDTO.Detail> requestCards
    ) {
        public static TaskResponseDTO.Detail from(Task task) {
            int completed = (int) task.getTodos().stream().filter(Todo::isDone).count();
            int pending = task.getTodos().size() - completed;

            List<MemberResponseDTO.Detail> memberDetails = task.getTaskMembers().stream()
                    .map(tm -> MemberResponseDTO.Detail.from(tm.getMember()))
                    .toList();

            return new TaskResponseDTO.Detail(
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getStatus(),
                    task.getStartDate(),
                    task.getEndDate(),
                    memberDetails,
                    completed,
                    pending,
                    task.getTodos().stream()
                            .map(TodoResponseDTO.Detail::from)
                            .toList(),
                    task.getRequestCards().stream()
                            .map(RequestCardResponseDTO.Detail::from)
                            .toList()
            );
        }
    }

    @Schema(description = "내 업무 전체 응답 DTO")
    public record MyTasksResponse(
            MemberResponseDTO.Detail myInfo,
            String projectName,
            int totalTodoCount,
            int completedTodoCount,
            int pendingTodoCount,
            List<MyTask> tasks
    ) {
    }

    @Schema(description = "개별 내 업무 정보 DTO")
    public record MyTask(
            Long taskId,
            String title,
            int totalTodoCount,
            int completedTodoCount,
            long daysLeft,
            String status,
            List<TodoResponseDTO.Detail> todos
    ) {
        public static MyTask from(Task task) {
            List<TodoResponseDTO.Detail> todos = task.getTodos().stream()
                    .map(TodoResponseDTO.Detail::from)
                    .toList();

            int total = todos.size();
            int completed = (int) todos.stream().filter(TodoResponseDTO.Detail::done).count();
            long daysLeft = getDaysLeft(task.getEndDate());

            return new MyTask(
                    task.getId(),
                    task.getTitle(),
                    total,
                    completed,
                    daysLeft,
                    task.getStatus().name(),
                    todos
            );
        }

        private static long getDaysLeft(LocalDate endDate) {
            return (endDate != null)
                    ? ChronoUnit.DAYS.between(LocalDate.now(), endDate)
                    : 0;
        }
    }

    @Schema(description = "카테고리 내 Task 요약 정보")
    public record Summary(
            Long id,
            String title,
            String status,
            List<MemberResponseDTO.Detail> members,
            @DateFormat LocalDate startDate,
            @DateFormat LocalDate endDate,
            int completedTodoCount,
            int pendingTodoCount
    ) {
        public static TaskResponseDTO.Summary from(Task task) {
            int completed = (int) task.getTodos().stream().filter(Todo::isDone).count();
            int pending = task.getTodos().size() - completed;

            List<MemberResponseDTO.Detail> memberDetails = task.getTaskMembers().stream()
                    .map(tm -> MemberResponseDTO.Detail.from(tm.getMember()))
                    .toList();

            return new TaskResponseDTO.Summary(
                    task.getId(),
                    task.getTitle(),
                    task.getStatus().name(),
                    memberDetails,
                    task.getStartDate(),
                    task.getEndDate(),
                    completed,
                    pending
            );
        }
    }
}
