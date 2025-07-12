package we.LiteBoard.domain.notification.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.member.repository.MemberRepository;
import we.LiteBoard.domain.notification.entity.ScheduledNotification;
import we.LiteBoard.domain.notification.repository.ScheduledNotificationRepository;
import we.LiteBoard.domain.notification.service.NotificationService;
import we.LiteBoard.domain.requestCard.entity.RequestCard;
import we.LiteBoard.domain.task.entity.Task;
import we.LiteBoard.domain.task.enumerate.Status;
import we.LiteBoard.domain.task.repository.TaskRepository;
import we.LiteBoard.domain.todo.entity.Todo;
import we.LiteBoard.domain.todo.repository.TodoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final TaskRepository taskRepository;
    private final NotificationService notificationService;
    private final MemberRepository memberRepository;
    private final TodoRepository todoRepository;
    private final ScheduledNotificationRepository scheduledNotificationRepository;

    @Scheduled(cron = "0 00 00 * * *", zone = "Asia/Seoul")
    @Transactional
    public void checkDelayedTasks() {
        List<Task> tasks = taskRepository.findTasksToCheckDelay();

        for (Task task : tasks) {
            boolean changed = task.refreshStatus();

            if (changed && task.getStatus() == Status.DELAYED) {
                notificationService.notifyTaskDelayed(task);
            }
        }
    }

    @Scheduled(cron = "0 00 06 * * *", zone = "Asia/Seoul")
    @Transactional
    public void sendTodayTodoSummary() {
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            List<Todo> todos = todoRepository.findTodayTodosByMember(member.getId());
            notificationService.notifyTodayTodoSummary(member, todos);
        }
    }

    @Scheduled(cron = "0 */30 * * * *", zone = "Asia/Seoul")
    @Transactional
    public void checkAndSendPendingNotifications() {
        List<ScheduledNotification> targets = scheduledNotificationRepository
                .findByNotifyTimeBeforeAndNotifiedFalse(LocalDateTime.now());

        for (ScheduledNotification s : targets) {
            RequestCard card = s.getRequestCard();

            if (!card.getTodos().isEmpty()) {
                notificationService.notifyUnregisteredTodos(card);
            }

            s.setNotified(true);
        }
    }

}
