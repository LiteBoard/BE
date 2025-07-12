package we.LiteBoard.domain.notification.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    TASK_ASSIGNED("업무 배정"),
    TASK_COMPLETED("업무 완료"),
    TASK_DELAYED("업무 지연"),
    TASK_DUE_DATE_CHANGED("마감일 변경"),

    TODO_ASSIGNED("협업 업무 배정"),
    TODO_COMPLETED("협업 완료"),

    REQUEST_CARD_CREATED("업무 요청 등록"),
    REQUEST_CARD_UPDATED("업무 요청 수정"),
    REQUEST_CARD_DELETED("업무 요청 삭제"),
    REQUEST_CARD_TODO_EMPTY("요청카드 미완성"),

    TODAY_TODO_SUMMARY("오늘의 할 일");

    private final String key;
}

