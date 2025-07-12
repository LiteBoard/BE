package we.LiteBoard.domain.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.notification.enumerate.NotificationType;

import java.time.LocalDateTime;

@Entity
@Table(name = "NOTIFICATION")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTIFICATION_ID")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(nullable = false)
    private String title;

    private String content;

    @Column(name = "RECEIVED_AT")
    private LocalDateTime receivedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SENDER_ID")
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECEIVER_ID", nullable = false)
    private Member receiver;

}
