package we.LiteBoard.domain.notification.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import we.LiteBoard.domain.notification.entity.ScheduledNotification;
import we.LiteBoard.domain.notification.repository.ScheduledNotificationRepository;
import we.LiteBoard.domain.notification.service.NotificationService;
import we.LiteBoard.domain.requestCard.entity.RequestCard;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PendingNotificationSender {

    private final ScheduledNotificationRepository scheduledNotificationRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 */30 * * * *", zone = "Asia/Seoul")
    @Transactional
    public void checkAndSendPendingNotifications() {
        List<ScheduledNotification> targets =
                scheduledNotificationRepository.findByNotifyTimeBeforeAndNotifiedFalse(LocalDateTime.now());

        for (ScheduledNotification s : targets) {
            RequestCard card = s.getRequestCard();

            if (!card.getTodos().isEmpty()) {
                notificationService.notifyUnregisteredTodos(card);
            }

            s.setNotified(true);
        }
    }
}
