package we.LiteBoard.domain.notification.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import we.LiteBoard.domain.task.entity.Task;

public interface NotificationService {
    SseEmitter subscribe(Long memberId);
    void notifyTaskAssigned(Task task);
}
