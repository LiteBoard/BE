package we.LiteBoard.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.notification.dto.NotificationResponseDTO;
import we.LiteBoard.domain.notification.entity.Notification;
import we.LiteBoard.domain.notification.enumerate.NotificationType;
import we.LiteBoard.domain.notification.repository.NotificationRepository;
import we.LiteBoard.domain.notification.sse.SseEmitterRepository;
import we.LiteBoard.domain.requestCard.entity.RequestCard;
import we.LiteBoard.domain.task.entity.Task;
import we.LiteBoard.domain.todo.entity.Todo;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
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
                "새 업무 배정",
                "당신이 '" + task.getTitle() + "'의 담당자로 지정되었습니다."
        );
    }

    @Override
    @Transactional
    public void notifyTaskCompleted(Task task) {
        sendNotification(
                task.getMember(),
                null,
                NotificationType.TASK_COMPLETED,
                "업무 완료됨",
                "당신에게 배정된 '" + task.getTitle() + "'이 완료되었습니다."
        );
    }

    @Override
    @Transactional
    public void notifyTaskDelayed(Task task) {
        String formattedDate = task.getEndDate().format(DateTimeFormatter.ofPattern("M/d"));
        sendNotification(
                task.getMember(),
                null,
                NotificationType.TASK_DELAYED,
                "업무 지연됨",
                "'" + task.getTitle() + "'의 마감일(" + formattedDate + ")이 지났지만 아직 완료되지 않았습니다."
        );
    }

    @Override
    @Transactional
    public void notifyTaskDueDateChanged(Task task) {
        String formattedDate = task.getEndDate().format(DateTimeFormatter.ofPattern("M월 d일"));
        sendNotification(
                task.getMember(),
                null,
                NotificationType.TASK_DUE_DATE_CHANGED,
                "마감일 변경됨",
                "'" + task.getTitle() + "'의 마감일이 " + formattedDate + "로 변경되었습니다."
        );
    }


    @Override
    @Transactional
    public void notifyTodoAssigned(Todo todo, Member sender) {
        Member receiver = todo.getMember();

        String title = sender.getName() + "의 TODO에 참여";
        String content = sender.getName() + "님이 당신을 '" + todo.getDescription() + "' TODO에 지정했습니다.";

        sendNotification(
                receiver,
                sender,
                NotificationType.TODO_ASSIGNED,
                title,
                content
        );
    }

    @Override
    @Transactional
    public void notifyTodoCompleted(Todo todo) {
        Member receiver = todo.getMember();
        String senderName = receiver.getName();

        String title = senderName + "의 협업 Todo 완료";
        String content = "'" + todo.getDescription() + "' 작업이 완료되었습니다.";

        sendNotification(
                receiver,
                null,
                NotificationType.TODO_COMPLETED,
                title,
                content
        );
    }

    @Override
    @Transactional
    public void notifyRequestCardCreated(RequestCard card) {
        Task task = card.getTask();
        Member receiver = task.getMember();

        sendNotification(
                receiver,
                card.getSender(),
                NotificationType.REQUEST_CARD_CREATED,
                "새 업무 요청",
                "당신의 업무 '" + task.getTitle() + "'에 요청 카드가 등록되었습니다."
        );
    }

    @Override
    @Transactional
    public void notifyUnregisteredTodos(RequestCard card) {
        String taskTitle = card.getTask().getTitle();

        sendNotification(
                card.getReceiver(),
                card.getSender(),
                NotificationType.REQUEST_CARD_TODO_EMPTY,
                "요청 미처리 항목 존재",
                "'" + taskTitle + "'의 요청 카드 내 미등록된 Todo가 남아 있습니다."
        );
    }

    @Override
    @Transactional
    public void notifyRequestCardUpdated(RequestCard card) {
        Task task = card.getTask();
        Member receiver = task.getMember();

        sendNotification(
                receiver,
                card.getSender(),
                NotificationType.REQUEST_CARD_UPDATED,
                "업무 요청 변경됨",
                "당신의 업무 '" + task.getTitle() + "'에 등록된 요청 카드가 수정되었습니다."
        );
    }


    @Override
    @Transactional
    public void notifyRequestCardDeleted(RequestCard card) {
        Task task = card.getTask();
        Member receiver = task.getMember();

        sendNotification(
                receiver,
                card.getSender(),
                NotificationType.REQUEST_CARD_DELETED,
                "업무 요청 삭제됨",
                "당신의 업무 '" + task.getTitle() + "'에 등록된 요청 카드가 삭제되었습니다."
        );
    }


    @Override
    @Transactional
    public void notifyTodayTodoSummary(Member receiver, List<Todo> todos) {
        if (todos.isEmpty()) return;

        StringBuilder content = new StringBuilder();
        content.append("오늘 해야 할 Todo")
                .append(todos.size())
                .append("개가 예정되어 있어요.");

        todos.stream()
                .limit(5)
                .forEach(todo -> content.append("\n").append(todo.getDescription()));

        sendNotification(
                receiver,
                null,
                NotificationType.TODAY_TODO_SUMMARY,
                "오늘의 Todo",
                content.toString()
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
        try {
            NotificationResponseDTO.Detail response = NotificationResponseDTO.Detail.from(notification);
            sseEmitterRepository.send(receiver.getId(), response);
        } catch (Exception e) {
            log.warn("[SSE 알림 실패] receiverId: {}, type: {}, reason: {}",
                    receiver.getId(), type, e.getMessage());
        }
    }
}
