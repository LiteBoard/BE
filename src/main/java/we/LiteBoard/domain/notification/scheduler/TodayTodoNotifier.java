package we.LiteBoard.domain.notification.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.member.repository.MemberRepository;
import we.LiteBoard.domain.notification.service.NotificationService;
import we.LiteBoard.domain.todo.entity.Todo;
import we.LiteBoard.domain.todo.repository.TodoRepository;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TodayTodoNotifier {

    private final MemberRepository memberRepository;
    private final TodoRepository todoRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Seoul")
    @Transactional
    public void sendTodayTodoSummary() {
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            try {
                List<Todo> todos = todoRepository.findTodayTodosByMember(member.getId());
                notificationService.notifyTodayTodoSummary(member, todos);
            } catch (Exception e) {
                log.error("Failed to send today's TODO summary to memberId={}", member.getId(), e);
            }
        }
    }
}