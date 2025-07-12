package we.LiteBoard.domain.notification.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import we.LiteBoard.domain.notification.entity.Notification;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public void save(Notification notification) {
        notificationJpaRepository.save(notification);
    }
}
