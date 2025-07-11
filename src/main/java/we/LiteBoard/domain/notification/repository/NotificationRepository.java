package we.LiteBoard.domain.notification.repository;

import we.LiteBoard.domain.notification.entity.Notification;

public interface NotificationRepository {
    void save(Notification notification);
}
