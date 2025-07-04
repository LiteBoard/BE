package we.LiteBoard.domain.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import we.LiteBoard.domain.task.dto.TaskRequestDTO;
import we.LiteBoard.domain.task.dto.TaskResponseDTO;
import we.LiteBoard.domain.task.service.TaskService;
import we.LiteBoard.global.response.SuccessResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "업무")
@RequestMapping("/api/v1")
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/categories/{categoryId}/tasks")
    @Operation(summary = "업무 생성", description = "카테고리에 업무를 생성합니다.")
    public SuccessResponse<TaskResponseDTO.Upsert> create(
            @PathVariable Long categoryId,
            @RequestBody @Valid TaskRequestDTO.Create request
    ) {
        return SuccessResponse.ok(taskService.create(categoryId, request));
    }

    @GetMapping("/categories/{categoryId}/tasks")
    @Operation(summary = "업무 목록 조회", description = "해당 카테고리에 속한 업무 목록을 반환합니다.")
    public SuccessResponse<List<TaskResponseDTO.Detail>> getAll(
            @PathVariable Long categoryId
    ) {
        return SuccessResponse.ok(taskService.getAllByCategoryId(categoryId));
    }

    @GetMapping("/tasks/{taskId}")
    @Operation(summary = "업무 단건 조회", description = "업무 ID로 단건 조회합니다.")
    public SuccessResponse<TaskResponseDTO.Detail> getById(
            @PathVariable Long taskId
    ) {
        return SuccessResponse.ok(taskService.getById(taskId));
    }

    @PatchMapping("/tasks/{taskId}")
    @Operation(summary = "업무 수정", description = "업무를 수정합니다.")
    public SuccessResponse<TaskResponseDTO.Upsert> update(
            @PathVariable Long taskId,
            @RequestBody @Valid TaskRequestDTO.Update request
    ) {
        return SuccessResponse.ok(taskService.update(taskId, request));
    }

    @DeleteMapping("/tasks/{taskId}")
    @Operation(summary = "업무 삭제", description = "업무를 삭제합니다.")
    public SuccessResponse<String> delete(
            @PathVariable Long taskId
    ) {
        taskService.deleteById(taskId);
        return SuccessResponse.ok("업무 삭제에 성공했습니다.");
    }
}
