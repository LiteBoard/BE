package we.LiteBoard.domain.requestCard.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import we.LiteBoard.domain.member.entity.QMember;
import we.LiteBoard.domain.requestCard.entity.RequestCard;
import we.LiteBoard.global.exception.CustomException;
import we.LiteBoard.global.exception.ErrorCode;

import java.util.List;

import static we.LiteBoard.domain.requestCard.entity.QRequestCard.requestCard;
import static we.LiteBoard.domain.requestCardTodo.entity.QRequestCardTodo.requestCardTodo;

@Repository
@RequiredArgsConstructor
public class RequestCardRepositoryImpl implements RequestCardRepository {

    private final RequestCardJpaRepository requestCardJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public void save(RequestCard requestCard) {
        requestCardJpaRepository.save(requestCard);
    }

    @Override
    public List<RequestCard> findAllByTaskId(Long taskId) {
        QMember sender = QMember.member;

        return queryFactory
                .selectFrom(requestCard)
                .leftJoin(requestCard.sender, sender).fetchJoin()
                .leftJoin(requestCard.todos, requestCardTodo).fetchJoin()
                .where(requestCard.task.id.eq(taskId))
                .distinct()
                .fetch();
    }

    @Override
    public RequestCard getById(Long requestCardId) {
        return requestCardJpaRepository.findById(requestCardId)
                .orElseThrow(() -> new CustomException(ErrorCode.REQUEST_CARD_NOT_FOUND));
    }

    @Override
    public void delete(Long requestCardId) {
        requestCardJpaRepository.deleteById(requestCardId);
    }
}
