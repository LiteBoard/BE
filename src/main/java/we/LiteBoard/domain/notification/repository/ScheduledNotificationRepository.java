package we.LiteBoard.domain.notification.repository;

import we.LiteBoard.domain.notification.entity.ScheduledNotification;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledNotificationRepository {
    ScheduledNotification save(ScheduledNotification build);
    List<ScheduledNotification> findByNotifyTimeBeforeAndNotifiedFalse(LocalDateTime now);
}
