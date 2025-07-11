package we.LiteBoard.domain.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;
import we.LiteBoard.domain.notification.entity.Notification;
import we.LiteBoard.domain.notification.enumerate.NotificationType;

import java.time.LocalDateTime;

@Schema(description = "알림 관련 응답 DTO")
public class NotificationResponseDTO {

    @Schema(description = "알림 상세 조회 응답 DTO")
    public record Detail(
            @Schema(description = "알림 ID") Long id,
            @Schema(description = "알림 타입") NotificationType type,
            @Schema(description = "알림 제목") String title,
            @Schema(description = "알림 내용") String content,
            @Schema(description = "수신 시간") @DateTimeFormat LocalDateTime receivedAt
    ) {
        public static NotificationResponseDTO.Detail from(Notification notification) {
            return new NotificationResponseDTO.Detail(
                    notification.getId(),
                    notification.getType(),
                    notification.getTitle(),
                    notification.getContent(),
                    notification.getReceivedAt()
            );
        }
    }
}
