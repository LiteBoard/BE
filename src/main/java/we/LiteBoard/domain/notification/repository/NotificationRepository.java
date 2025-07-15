package we.LiteBoard.domain.notification.repository;

import we.LiteBoard.domain.notification.entity.Notification;

import java.util.List;

public interface NotificationRepository {
    void save(Notification notification);
    List<Notification> findByReceiverId(Long memberId);
}
