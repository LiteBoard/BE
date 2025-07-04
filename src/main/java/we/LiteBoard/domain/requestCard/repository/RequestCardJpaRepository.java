package we.LiteBoard.domain.requestCard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import we.LiteBoard.domain.requestCard.entity.RequestCard;

public interface RequestCardJpaRepository extends JpaRepository<RequestCard, Long> {
}
