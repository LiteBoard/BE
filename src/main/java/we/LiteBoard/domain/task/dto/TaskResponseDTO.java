package we.LiteBoard.domain.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import we.LiteBoard.domain.member.dto.MemberResponseDTO;
import we.LiteBoard.domain.requestCard.dto.RequestCardResponseDTO;
import we.LiteBoard.domain.task.entity.Task;
import we.LiteBoard.domain.task.enumerate.Status;
import we.LiteBoard.domain.todo.dto.TodoResponseDTO;
import we.LiteBoard.global.common.annotation.DateFormat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Schema(description = "업무 관련 응답 DTO")
public class TaskResponseDTO {

    @Schema(description = "업무 생성 응답 DTO")
    public record Upsert(
            @Schema(description = "업무 ID") Long id
    ) {
        public static TaskResponseDTO.Upsert from(Long taskId) {
            return new TaskResponseDTO.Upsert(
                    taskId
            );
        }
    }

    @Schema(description = "업무 상세 조회 응답 DTO")
    public record Detail(
            @Schema(description = "업무 ID") Long id,
            @Schema(description = "업무 제목") String title,
            @Schema(description = "업무 설명") String description,
            @Schema(description = "상태") Status status,
            @Schema(description = "시작일") @DateFormat LocalDate startDate,
            @Schema(description = "마감일") @DateFormat LocalDate endDate,
            @Schema(description = "담당자 정보") MemberResponseDTO.Summary member,
            @Schema(description = "TODO 목록") List<TodoResponseDTO.Detail> todos,
            @Schema(description = "업무 요청 목록") List<RequestCardResponseDTO.Detail> requestCards
    ) {
        public static TaskResponseDTO.Detail from(Task task) {
            return new TaskResponseDTO.Detail(
                    task.getId(),
                    task.getTitle(),
                    task.getDescription(),
                    task.getStatus(),
                    task.getStartDate(),
                    task.getEndDate(),
                    MemberResponseDTO.Summary.from(task.getMember()),
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
            @Schema(description = "전체 Todo 수") int totalTodoCount,
            @Schema(description = "완료된 Todo 수") int completedTodoCount,
            @Schema(description = "미완료 Todo 수") int pendingTodoCount,
            @Schema(description = "내 업무 목록") List<MyTask> tasks
    ) {
    }

    @Schema(description = "개별 내 업무 정보 DTO")
    public record MyTask(
            @Schema(description = "업무 ID") Long taskId,
            @Schema(description = "업무 제목") String title,
            @Schema(description = "업무에 포함된 Todo 총 수") int totalTodoCount,
            @Schema(description = "완료된 Todo 수") int completedTodoCount,
            @Schema(description = "남은 기간(일)") long daysLeft,
            @Schema(description = "업무 상태") String status,
            @Schema(description = "업무에 속한 Todo 목록") List<TodoResponseDTO.Detail> todos
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
}
