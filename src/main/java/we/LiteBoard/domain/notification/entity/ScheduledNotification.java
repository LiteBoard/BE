package we.LiteBoard.domain.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import we.LiteBoard.domain.requestCard.entity.RequestCard;

import java.time.LocalDateTime;

@Entity
@Table(name = "SCHEDULED_NOTIFICATION")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduledNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULED_NOTIFICATION_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REQUEST_CARD_ID", nullable = false)
    private RequestCard requestCard;

    @Column(name = "NOTIFY_TIME")
    private LocalDateTime notifyTime;

    @Column(nullable = false)
    private boolean notified;

    public void assignNotified(boolean notified) {
        this.notified = notified;
    }

    public void assignRequestCard(RequestCard requestCard) {
        this.requestCard = requestCard;
    }
}
