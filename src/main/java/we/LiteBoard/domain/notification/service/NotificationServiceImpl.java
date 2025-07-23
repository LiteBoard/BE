package we.LiteBoard.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.notification.application.NotificationMessage;
import we.LiteBoard.domain.notification.application.NotificationMessageFactory;
import we.LiteBoard.domain.notification.application.NotificationSender;
import we.LiteBoard.domain.notification.dto.NotificationResponseDTO;
import we.LiteBoard.domain.notification.repository.NotificationRepository;
import we.LiteBoard.domain.notification.sse.SseEmitterRepository;
import we.LiteBoard.domain.requestCard.entity.RequestCard;
import we.LiteBoard.domain.task.entity.Task;
import we.LiteBoard.domain.taskMember.entity.TaskMember;
import we.LiteBoard.domain.todo.entity.Todo;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationSender sender;
    private final SseEmitterRepository sseEmitterRepository;
    private final NotificationRepository notificationRepository;

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
            log.error("Failed to send initial SSE event to member {}", memberId, e);
            sseEmitterRepository.delete(memberId);
            throw new IllegalStateException("SSE connect Fail", e);
        }

        return emitter;
    }

    @Override
    public List<NotificationResponseDTO.Detail> getMyNotifications(Long memberId) {
        return notificationRepository.findByReceiverId(memberId).stream()
                .map(NotificationResponseDTO.Detail::from)
                .toList();
    }

    @Override
    @Transactional
    public void notifyTaskAssigned(Task task, Member senderMember) {
        NotificationMessage message = NotificationMessageFactory.createTaskAssigned(task);
        for (TaskMember tm : task.getTaskMembers()) {
            sender.send(tm.getMember(), senderMember, message);
        }
    }

    @Override
    @Transactional
    public void notifyTaskCompleted(Task task) {
        NotificationMessage message = NotificationMessageFactory.createTaskCompleted(task);
        for (TaskMember tm : task.getTaskMembers()) {
            sender.send(tm.getMember(), null, message);
        }
    }

    @Override
    @Transactional
    public void notifyTaskDelayed(Task task) {
        NotificationMessage message = NotificationMessageFactory.createTaskDelayed(task);
        for (TaskMember tm : task.getTaskMembers()) {
            sender.send(tm.getMember(), null, message);
        }
    }

    @Override
    @Transactional
    public void notifyTaskDueDateChanged(Task task) {
        NotificationMessage message = NotificationMessageFactory.createTaskDueDateChanged(task);
        for (TaskMember tm : task.getTaskMembers()) {
            sender.send(tm.getMember(), null, message);
        }
    }

    @Override
    @Transactional
    public void notifyTodoAssigned(Todo todo, Member senderMember) {
        NotificationMessage message = NotificationMessageFactory.createTodoAssigned(todo, senderMember);
        sender.send(todo.getMember(), senderMember, message);
    }

    @Override
    @Transactional
    public void notifyTodoCompleted(Todo todo) {
        NotificationMessage message = NotificationMessageFactory.createTodoCompleted(todo);
        sender.send(todo.getMember(), null, message);
    }

    @Override
    @Transactional
    public void notifyTodayTodoSummary(Member receiver, List<Todo> todos) {
        if (todos.isEmpty()) return;

        NotificationMessage message = NotificationMessageFactory.createTodayTodo(todos);
        sender.send(receiver, null, message);
    }

    @Override
    @Transactional
    public void notifyRequestCardCreatedTo(Member receiver, Member senderMember, RequestCard requestCard) {
        NotificationMessage message = NotificationMessageFactory.createRequestCardCreated(requestCard);
        sender.send(receiver, senderMember, message);
    }

    @Override
    @Transactional
    public void notifyRequestCardUpdated(RequestCard card) {
        NotificationMessage message = NotificationMessageFactory.createRequestCardUpdated(card);
        for (TaskMember tm : card.getTask().getTaskMembers()) {
            sender.send(tm.getMember(), card.getSender(), message);
        }
    }

    @Override
    @Transactional
    public void notifyRequestCardDeleted(RequestCard card) {
        NotificationMessage message = NotificationMessageFactory.createRequestCardDeleted(card);
        for (TaskMember tm : card.getTask().getTaskMembers()) {
            sender.send(tm.getMember(), card.getSender(), message);
        }
    }

    @Override
    @Transactional
    public void notifyUnregisteredTodos(RequestCard card) {
        NotificationMessage message = NotificationMessageFactory.createUnregisteredTodos(card);
        sender.send(card.getReceiver(), card.getSender(), message);
    }
}
