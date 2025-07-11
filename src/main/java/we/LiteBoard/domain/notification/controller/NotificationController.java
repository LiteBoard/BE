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
import we.LiteBoard.domain.notification.service.NotificationServiceImpl;
import we.LiteBoard.global.common.annotation.CurrentMember;

@RestController
@RequiredArgsConstructor
@Tag(name = "알림")
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationServiceImpl notificationService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "알림 SSE 구독", description = "SSE 연결을 통해 로그인된 사용자가 실시간 알림을 수신합니다.")
    public SseEmitter subscribe(@CurrentMember Member currentMember) {
        return notificationService.subscribe(currentMember.getId());
    }
}