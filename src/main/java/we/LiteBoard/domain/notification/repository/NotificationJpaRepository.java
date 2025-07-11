package we.LiteBoard.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import we.LiteBoard.domain.notification.entity.Notification;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
}
