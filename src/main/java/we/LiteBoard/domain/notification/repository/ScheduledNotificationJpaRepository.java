package we.LiteBoard.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import we.LiteBoard.domain.notification.entity.ScheduledNotification;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledNotificationJpaRepository extends JpaRepository<ScheduledNotification, Long> {
    List<ScheduledNotification> findByNotifyTimeBeforeAndNotifiedFalse(LocalDateTime now);
}
