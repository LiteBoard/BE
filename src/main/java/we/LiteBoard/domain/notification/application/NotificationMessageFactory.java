package we.LiteBoard.domain.notification.application;

import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.notification.enumerate.NotificationType;
import we.LiteBoard.domain.requestCard.entity.RequestCard;
import we.LiteBoard.domain.task.entity.Task;
import we.LiteBoard.domain.todo.entity.Todo;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class NotificationMessageFactory {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("M/d");
    private static final DateTimeFormatter DATE_FORMAT_KR = DateTimeFormatter.ofPattern("M월 d일");

    public static NotificationMessage createTaskAssigned(Task task) {
        final String title = "새 업무 배정";
        final String content = String.format("당신이 '%s'의 담당자로 지정되었습니다.", task.getTitle());
        return new NotificationMessage(title, content, NotificationType.TASK_ASSIGNED);
    }

    public static NotificationMessage createTaskCompleted(Task task) {
        final String title = "업무 완료됨";
        final String content = String.format("당신에게 배정된 '%s'이 완료되었습니다.", task.getTitle());
        return new NotificationMessage(title, content, NotificationType.TASK_COMPLETED);
    }

    public static NotificationMessage createTaskDelayed(Task task) {
        final String title = "업무 지연됨";
        final String content = String.format(
                "'%s'의 마감일(%s)이 지났지만 아직 완료되지 않았습니다.",
                task.getTitle(), task.getEndDate().format(DATE_FORMAT)
        );
        return new NotificationMessage(title, content, NotificationType.TASK_DELAYED);
    }

    public static NotificationMessage createTaskDueDateChanged(Task task) {
        final String title = "마감일 변경됨";
        final String content = String.format(
                "'%s'의 마감일이 %s로 변경되었습니다.",
                task.getTitle(), task.getEndDate().format(DATE_FORMAT_KR)
        );
        return new NotificationMessage(title, content, NotificationType.TASK_DUE_DATE_CHANGED);
    }

    public static NotificationMessage createTodoAssigned(Todo todo, Member sender) {
        final String title = sender.getName() + "의 TODO에 참여";
        final String content = String.format("%s님이 당신을 '%s' TODO에 지정했습니다.",
                sender.getName(), todo.getDescription());
        return new NotificationMessage(title, content, NotificationType.TODO_ASSIGNED);
    }

    public static NotificationMessage createTodoCompleted(Todo todo) {
        final String title = todo.getMember().getName() + "의 협업 Todo 완료";
        final String content = String.format("'%s' 작업이 완료되었습니다.", todo.getDescription());
        return new NotificationMessage(title, content, NotificationType.TODO_COMPLETED);
    }

    public static NotificationMessage createTodayTodo(List<Todo> todos) {
        final StringBuilder content = new StringBuilder();
        content.append("오늘 해야 할 Todo ").append(todos.size()).append("개가 예정되어 있어요.");

        todos.stream()
                .limit(5)
                .forEach(todo -> content.append("\n").append(todo.getDescription()));

        return new NotificationMessage("오늘의 Todo", content.toString(), NotificationType.TODAY_TODO_SUMMARY);
    }

    public static NotificationMessage createRequestCardCreated(RequestCard card) {
        final String title = "새 업무 요청";
        final String content = String.format("당신의 업무 '%s'에 요청 카드가 등록되었습니다.",
                card.getTask().getTitle());
        return new NotificationMessage(title, content, NotificationType.REQUEST_CARD_CREATED);
    }

    public static NotificationMessage createUnregisteredTodos(RequestCard card) {
        final String title = "요청 미처리 항목 존재";
        final String content = String.format("'%s'의 요청 카드 내 미등록된 Todo가 남아 있습니다.",
                card.getTask().getTitle());
        return new NotificationMessage(title, content, NotificationType.REQUEST_CARD_TODO_EMPTY);
    }

    public static NotificationMessage createRequestCardUpdated(RequestCard card) {
        final String title = "업무 요청 변경됨";
        final String content = String.format("당신의 업무 '%s'에 등록된 요청 카드가 수정되었습니다.",
                card.getTask().getTitle());
        return new NotificationMessage(title, content, NotificationType.REQUEST_CARD_UPDATED);
    }

    public static NotificationMessage createRequestCardDeleted(RequestCard card) {
        final String title = "업무 요청 삭제됨";
        final String content = String.format("당신의 업무 '%s'에 등록된 요청 카드가 삭제되었습니다.",
                card.getTask().getTitle());
        return new NotificationMessage(title, content, NotificationType.REQUEST_CARD_DELETED);
    }
}
