package we.LiteBoard.domain.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.notification.dto.NotificationResponseDTO;
import we.LiteBoard.domain.notification.entity.Notification;
import we.LiteBoard.domain.notification.enumerate.NotificationType;
import we.LiteBoard.domain.notification.repository.NotificationRepository;
import we.LiteBoard.domain.notification.sse.SseEmitterRepository;
import we.LiteBoard.domain.task.entity.Task;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SseEmitterRepository sseEmitterRepository;

    /**
     * SSE 구독
     * @param memberId SSE 구독한 사용자 ID (현재 로그인 유저)
     * @return SseEmitter 객체를 반환하고 서버와 클라이언트 간 실시간 알림 스트림 연결을 유지
     */
    @Override
    @Transactional
    public SseEmitter subscribe(Long memberId) {
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L); // 1시간 유지
        sseEmitterRepository.save(memberId, emitter);

        emitter.onCompletion(() -> sseEmitterRepository.delete(memberId));
        emitter.onTimeout(() -> sseEmitterRepository.delete(memberId));
        emitter.onError((e) -> sseEmitterRepository.delete(memberId));

        // 초기 응답을 주기 위한 더미 데이터
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("Success on SSE connect")
                    .id(String.valueOf(System.currentTimeMillis())));
        } catch (IOException e) {
            throw new RuntimeException("SSE connect Fail", e);
        }

        return emitter;
    }

    /**
     * 업무 배정 알림 전송
     * @param task 배정된 업무
     */
    @Override
    @Transactional
    public void notifyTaskAssigned(Task task, Member sender) {
        Member receiver = task.getMember();

        sendNotification(
                receiver,
                sender,
                NotificationType.TASK_ASSIGNED,
                "새로운 업무가 배정되었습니다.",
                "업무 제목: " + task.getTitle()
        );
    }

    private void sendNotification(
            Member receiver,
            Member sender,
            NotificationType type,
            String title,
            String content
    ) {
        Notification notification = Notification.builder()
                .title(title)
                .content(content)
                .receiver(receiver)
                .sender(sender)
                .type(type)
                .receivedAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
        NotificationResponseDTO.Detail response = NotificationResponseDTO.Detail.from(notification);
        sseEmitterRepository.send(receiver.getId(), response);
    }
}
