package we.LiteBoard.domain.notification.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import we.LiteBoard.domain.notification.entity.Notification;

import java.util.List;

import static we.LiteBoard.domain.notification.entity.QNotification.notification;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public void save(Notification notification) {
        notificationJpaRepository.save(notification);
    }

    @Override
    public List<Notification> findByReceiverId(Long memberId) {
        return queryFactory
                .selectFrom(notification)
                .where(notification.receiver.id.eq(memberId))
                .orderBy(notification.receivedAt.desc())
                .fetch();
    }
}
