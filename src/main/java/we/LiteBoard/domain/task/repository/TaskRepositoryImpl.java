package we.LiteBoard.domain.task.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.task.entity.Task;
import we.LiteBoard.domain.task.enumerate.Status;
import we.LiteBoard.global.exception.CustomException;
import we.LiteBoard.global.exception.ErrorCode;

import java.util.List;

import static we.LiteBoard.domain.task.entity.QTask.task;
import static we.LiteBoard.domain.taskMember.entity.QTaskMember.taskMember;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final TaskJpaRepository taskJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Task save(Task task) {
        return taskJpaRepository.save(task);
    }

    @Override
    public List<Task> findAllByCategoryId(Long categoryId) {
        return queryFactory
                .selectFrom(task)
                .where(task.category.id.eq(categoryId))
                .orderBy(task.endDate.asc().nullsLast())
                .fetch();
    }

    @Override
    public Task getById(Long taskId) {
        return taskJpaRepository.findById(taskId)
                .orElseThrow(() -> new CustomException(ErrorCode.TASK_NOT_FOUND));
    }

    @Override
    public void deleteById(Long taskId) {
        taskJpaRepository.deleteById(taskId);
    }

    @Override
    public List<Task> findByMemberAndStatuses(Member member, List<Status> statuses) {
        return queryFactory
                .select(task)
                .from(taskMember)
                .join(taskMember.task, task)
                .where(
                        taskMember.member.eq(member),
                        task.status.in(statuses)
                )
                .orderBy(task.endDate.asc().nullsLast())
                .fetch();
    }

    @Override
    public List<Task> findTasksToCheckDelay() {
        return queryFactory
                .selectFrom(task)
                .where(task.status.ne(Status.COMPLETED)
                        .and(task.status.ne(Status.DELAYED)))
                .fetch();
    }
}
