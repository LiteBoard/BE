package we.LiteBoard.domain.memberProject.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProjectRole {
    ADMIN("관리자"),
    EDITOR("편집자"),
    VIEWER("조회자");

    private final String key;
}
