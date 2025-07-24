package we.LiteBoard.domain.notification.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import we.LiteBoard.domain.notification.entity.ScheduledNotification;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScheduledNotificationRepositoryImpl implements ScheduledNotificationRepository {

    private final ScheduledNotificationJpaRepository scheduledNotificationJpaRepository;

    @Override
    public ScheduledNotification save(ScheduledNotification build) {
        return scheduledNotificationJpaRepository.save(build);
    }

    @Override
    public List<ScheduledNotification> findByNotifyTimeBeforeAndNotifiedFalse(LocalDateTime now) {
        return scheduledNotificationJpaRepository.findByNotifyTimeBeforeAndNotifiedFalse(now);
    }
}
