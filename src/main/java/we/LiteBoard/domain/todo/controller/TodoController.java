package we.LiteBoard.domain.todo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.todo.dto.TodoRequestDTO;
import we.LiteBoard.domain.todo.dto.TodoResponseDTO;
import we.LiteBoard.domain.todo.service.TodoService;
import we.LiteBoard.global.common.annotation.CurrentMember;
import we.LiteBoard.global.response.SuccessResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "TODO")
@RequestMapping("/api/v1")
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/tasks/{taskId}/todos")
    @Operation(summary = "TODO 생성", description = "업무에 TODO를 생성합니다.")
    public SuccessResponse<TodoResponseDTO.Upsert> create(
            @PathVariable Long taskId,
            @RequestBody @Valid TodoRequestDTO.Upsert request
    ) {
        return SuccessResponse.ok(todoService.create(taskId, request));
    }

    @GetMapping("/tasks/{taskId}/todos")
    @Operation(summary = "Task에 속한 TODO 목록 조회", description = "특정 업무에 속한 TODO 목록을 반환합니다.")
    public SuccessResponse<List<TodoResponseDTO.Detail>> getAllByTask(@PathVariable Long taskId) {
        return SuccessResponse.ok(todoService.getAllByTask(taskId));
    }

    @PatchMapping("/todos/{todoId}")
    @Operation(summary = "TODO 수정", description = "TODO 내용을 수정합니다.")
    public SuccessResponse<TodoResponseDTO.Upsert> update(
            @PathVariable Long todoId,
            @RequestBody @Valid TodoRequestDTO.Upsert request,
            @CurrentMember Member member
    ) {
        return SuccessResponse.ok(todoService.update(todoId, request, member));
    }

    @PatchMapping("/todos/toggle")
    @Operation(summary = "여러 TODO 완료 상태 토글", description = "전달받은 todoId 리스트의 완료 상태를 반전시킵니다.")
    public SuccessResponse<List<TodoResponseDTO.Detail>> toggleTodos(
            @RequestBody @Valid TodoRequestDTO.Toggle request
    ) {
        return SuccessResponse.ok(todoService.toggleTodos(request.todoIds()));
    }

    @DeleteMapping("/todos/{todoId}")
    @Operation(summary = "TODO 삭제", description = "TODO를 삭제합니다.")
    public SuccessResponse<String> delete(@PathVariable Long todoId) {
        todoService.deleteById(todoId);
        return SuccessResponse.ok("TODO 삭제에 성공했습니다.");
    }
}
