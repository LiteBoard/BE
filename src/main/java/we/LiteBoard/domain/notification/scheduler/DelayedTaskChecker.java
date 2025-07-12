package we.LiteBoard.domain.notification.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import we.LiteBoard.domain.notification.service.NotificationService;
import we.LiteBoard.domain.task.entity.Task;
import we.LiteBoard.domain.task.enumerate.Status;
import we.LiteBoard.domain.task.repository.TaskRepository;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DelayedTaskChecker {

    private final TaskRepository taskRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void checkDelayedTasks() {
        List<Task> tasks = taskRepository.findTasksToCheckDelay();

        for (Task task : tasks) {
            if (task.refreshStatus() && task.getStatus() == Status.DELAYED) {
                try {
                    notificationService.notifyTaskDelayed(task);
                } catch (Exception e) {
                    log.error("Failed to send delayed task notification for taskId={}", task.getId(), e);
                }
            }
        }
    }
}
