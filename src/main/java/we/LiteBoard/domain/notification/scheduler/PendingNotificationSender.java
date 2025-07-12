package we.LiteBoard.domain.notification.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import we.LiteBoard.domain.notification.entity.ScheduledNotification;
import we.LiteBoard.domain.notification.repository.ScheduledNotificationRepository;
import we.LiteBoard.domain.notification.service.NotificationService;
import we.LiteBoard.domain.requestCard.entity.RequestCard;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
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
            try {
                RequestCard card = s.getRequestCard();

                if (!card.getTodos().isEmpty()) {
                    notificationService.notifyUnregisteredTodos(card);
                }

                s.setNotified(true);

            } catch (Exception e) {
                log.error("Failed to notify unregistered todos. RequestCardId={}, ScheduledNotificationId={}",
                        s.getRequestCard().getId(), s.getId(), e);
            }
        }
    }
}
