package we.LiteBoard.domain.notification.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.notification.dto.NotificationResponseDTO;
import we.LiteBoard.domain.requestCard.entity.RequestCard;
import we.LiteBoard.domain.task.entity.Task;
import we.LiteBoard.domain.todo.entity.Todo;

import java.util.List;

public interface NotificationService {
    SseEmitter subscribe(Long memberId);
    List<NotificationResponseDTO.Detail> getMyNotifications(Long memberId);
    void notifyTaskAssigned(Task task, Member sender);
    void notifyTaskCompleted(Task task);
    void notifyTaskDelayed(Task task);
    void notifyTaskDueDateChanged(Task task);
    void notifyTodoAssigned(Todo todo, Member collaborator);
    void notifyTodoCompleted(Todo todo);
    void notifyRequestCardCreated(RequestCard card);
    void notifyUnregisteredTodos(RequestCard card);
    void notifyRequestCardUpdated(RequestCard card);
    void notifyRequestCardDeleted(RequestCard card);
    void notifyTodayTodoSummary(Member receiver, List<Todo> todos);
}
