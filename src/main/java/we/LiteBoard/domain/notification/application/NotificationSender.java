package we.LiteBoard.domain.notification.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.notification.dto.NotificationResponseDTO;
import we.LiteBoard.domain.notification.entity.Notification;
import we.LiteBoard.domain.notification.repository.NotificationRepository;
import we.LiteBoard.domain.notification.sse.SseEmitterRepository;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSender {

    private final NotificationRepository notificationRepository;
    private final SseEmitterRepository sseEmitterRepository;

    public void send(Member receiver, Member sender, NotificationMessage message) {
        Notification notification = Notification.builder()
                .title(message.title())
                .content(message.content())
                .receiver(receiver)
                .sender(sender)
                .type(message.type())
                .receivedAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        try {
            NotificationResponseDTO.Detail response = NotificationResponseDTO.Detail.from(notification);
            sseEmitterRepository.send(receiver.getId(), response);
        } catch (Exception e) {
            log.warn("[SSE 알림 실패] receiverId: {}, type: {}, reason: {}",
                    receiver.getId(), message.type(), e.getMessage());
        }
    }
}

