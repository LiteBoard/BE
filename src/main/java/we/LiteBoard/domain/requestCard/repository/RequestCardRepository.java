package we.LiteBoard.domain.requestCard.repository;

import we.LiteBoard.domain.requestCard.entity.RequestCard;

import java.util.List;

public interface RequestCardRepository {
    void save(RequestCard requestCard);
    List<RequestCard> findAllByTaskId(Long taskId);
    RequestCard getById(Long requestCardId);
    void delete(RequestCard requestCard);
}
