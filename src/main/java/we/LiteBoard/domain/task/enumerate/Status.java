package we.LiteBoard.domain.task.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    PENDING("대기중"),
    IN_PROGRESS("진행중"),
    COMPLETED("완료"),
    DELAYED("지연됨");

    private final String key;
}
