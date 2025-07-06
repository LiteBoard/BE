package we.LiteBoard.domain.requestCardTodo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.requestCardTodo.service.RequestCardTodoService;
import we.LiteBoard.global.common.annotation.CurrentMember;
import we.LiteBoard.global.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "업무 요청 TODO")
public class RequestCardTodoController {

    private final RequestCardTodoService requestCardTodoService;

    @PostMapping("/request-cards/{requestCardId}/todos/{requestCardTodoId}/accept")
    @Operation(summary = "요청된 할 일 수락", description = "요청된 특정 Todo 항목을 수락하여 내 Todo로 추가합니다.")
    public SuccessResponse<Long> acceptRequestCardTodo(
            @CurrentMember Member currentMember,
            @PathVariable Long requestCardId,
            @PathVariable Long requestCardTodoId
    ) {
        Long createdTodoId = requestCardTodoService.acceptTodo(requestCardId, requestCardTodoId, currentMember);
        return SuccessResponse.ok(createdTodoId);
    }
}
