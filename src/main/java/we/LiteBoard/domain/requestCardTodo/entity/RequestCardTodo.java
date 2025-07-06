package we.LiteBoard.domain.requestCardTodo.entity;

import jakarta.persistence.*;
import lombok.*;
import we.LiteBoard.domain.requestCard.entity.RequestCard;

@Entity
@Table(name = "REQUEST_CARD_TODO")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestCardTodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQUEST_CARD_TODO_ID")
    private Long id;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REQUEST_CARD_ID", nullable = false)
    private RequestCard requestCard;

    public void setRequestCard(RequestCard requestCard) {
        this.requestCard = requestCard;
    }
}
