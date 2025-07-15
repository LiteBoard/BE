package we.LiteBoard.domain.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.notification.dto.NotificationResponseDTO;
import we.LiteBoard.domain.notification.service.NotificationService;
import we.LiteBoard.global.common.annotation.CurrentMember;
import we.LiteBoard.global.response.SuccessResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "알림")
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "알림 SSE 구독", description = "SSE 연결을 통해 로그인된 사용자가 실시간 알림을 수신합니다.")
    public SseEmitter subscribe(@CurrentMember Member currentMember) {
        return notificationService.subscribe(currentMember.getId());
    }

    @GetMapping
    @Operation(summary = "내 수신함 조회", description = "현재 로그인한 사용자의 알림 목록을 반환합니다.")
    public SuccessResponse<List<NotificationResponseDTO.Detail>> getMyNotifications(@CurrentMember Member currentMember) {
        List<NotificationResponseDTO.Detail> notifications = notificationService.getMyNotifications(currentMember.getId());
        return SuccessResponse.ok(notifications);
    }

}