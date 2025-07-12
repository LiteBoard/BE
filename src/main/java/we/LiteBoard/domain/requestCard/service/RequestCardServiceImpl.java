package we.LiteBoard.domain.requestCard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.notification.entity.ScheduledNotification;
import we.LiteBoard.domain.notification.repository.ScheduledNotificationRepository;
import we.LiteBoard.domain.notification.service.NotificationService;
import we.LiteBoard.domain.requestCard.dto.RequestCardRequestDTO;
import we.LiteBoard.domain.requestCard.dto.RequestCardResponseDTO;
import we.LiteBoard.domain.requestCard.entity.RequestCard;
import we.LiteBoard.domain.requestCard.repository.RequestCardRepository;
import we.LiteBoard.domain.requestCardTodo.entity.RequestCardTodo;
import we.LiteBoard.domain.task.entity.Task;
import we.LiteBoard.domain.task.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestCardServiceImpl implements RequestCardService {

    private final RequestCardRepository requestCardRepository;
    private final TaskRepository taskRepository;
    private final NotificationService notificationService;
    private final ScheduledNotificationRepository scheduledNotificationRepository;

    /**
     * 업무 요청 생성
     * @param sender 업무 요청을 하는 사람
     * @param taskId 업무 요청을 생성하는 상위 작업 ID
     * @param request 업무 요청 내용
     * @return 생성된 업무 요청 ID
     */
    @Override
    @Transactional
    public RequestCardResponseDTO.Upsert create(Member sender, Long taskId, RequestCardRequestDTO.Create request) {
        Task task = taskRepository.getById(taskId);
        Member receiver = task.getMember();

        RequestCard requestCard = RequestCard.builder()
                .content(request.content())
                .sender(sender)
                .receiver(receiver)
                .task(task)
                .build();

        for (String description : request.todoDescriptions()) {
            RequestCardTodo todo = RequestCardTodo.builder()
                    .description(description)
                    .build();
            requestCard.addTodo(todo);
        }

        requestCardRepository.save(requestCard);
        notificationService.notifyRequestCardCreated(requestCard);

        // 미등록 TODOs 검증 알림용 데이터 생성
        scheduledNotificationRepository.save(
                ScheduledNotification.builder()
                        .requestCard(requestCard)
                        .notifyTime(LocalDateTime.now().plusHours(3))
                        .notified(false)
                        .build()
        );
        return RequestCardResponseDTO.Upsert.from(requestCard);
    }

    /**
     * 엄무 요청 수정
     * @param requestCardId 수정할 업무 요청 ID
     * @param request 수정 사항
     * @return 수정된 업무 요청 ID
     */
    @Override
    @Transactional
    public RequestCardResponseDTO.Upsert update(Long requestCardId, RequestCardRequestDTO.Update request) {
        RequestCard requestCard = requestCardRepository.getById(requestCardId);
        requestCard.updateContent(request.content());

        List<RequestCardTodo> currentTodos = requestCard.getTodos();
        List<String> updatedDescriptions = request.todoDescriptions();

        // 삭제 대상 찾기
        List<RequestCardTodo> toRemove = currentTodos.stream()
                .filter(todo -> !updatedDescriptions.contains(todo.getDescription()))
                .toList();

        // 추가 대상 찾기
        List<String> toAdd = updatedDescriptions.stream()
                .filter(desc -> currentTodos.stream().noneMatch(todo -> todo.getDescription().equals(desc)))
                .toList();

        toRemove.forEach(requestCard::removeTodo);

        for (String description : toAdd) {
            RequestCardTodo todo = RequestCardTodo.builder()
                    .description(description)
                    .build();
            requestCard.addTodo(todo);
        }

        notificationService.notifyRequestCardUpdated(requestCard);
        return RequestCardResponseDTO.Upsert.from(requestCard);
    }

    /**
     * 특정 업무의 업무 요청 전체 조회
     * @param taskId 조회할 업무 ID
     * @return 조회된 업무 요청 리스트
     */
    @Override
    public List<RequestCardResponseDTO.Detail> getAllByTaskId(Long taskId) {
        List<RequestCard> requestCards = requestCardRepository.findAllByTaskId(taskId);
        return requestCards.stream()
                .map(RequestCardResponseDTO.Detail::from)
                .toList();
    }

    /**
     * 업무 요청 삭제
     * @param requestCardId 삭제할 업무 요청 ID
     */
    @Override
    @Transactional
    public void deleteById(Long requestCardId) {
        RequestCard requestCard = requestCardRepository.getById(requestCardId);
        notificationService.notifyRequestCardDeleted(requestCard);
        requestCardRepository.delete(requestCard);
    }
}
