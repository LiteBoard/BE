package we.LiteBoard.domain.task.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.task.dto.TaskRequestDTO;
import we.LiteBoard.domain.task.dto.TaskResponseDTO;
import we.LiteBoard.domain.task.service.TaskService;
import we.LiteBoard.global.common.annotation.CurrentMember;
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

    @PostMapping("/tasks/{taskId}/members")
    @Operation(summary = "업무 담당자 배정", description = "해당 업무에 담당자를 배정합니다.")
    public SuccessResponse<String> assignMembers(
            @PathVariable Long taskId,
            @RequestBody @Valid TaskRequestDTO.AssignMembers request,
            @CurrentMember Member member
    ) {
        taskService.assignMembers(taskId, request.memberIds(), member);
        return SuccessResponse.ok("담당자 배정 완료");
    }

    @DeleteMapping("/tasks/{taskId}/members/{memberId}")
    @Operation(summary = "업무 담당자 제거", description = "해당 업무에서 지정한 담당자를 제거합니다.")
    public SuccessResponse<String> removeMemberFromTask(
            @PathVariable Long taskId,
            @PathVariable Long memberId
    ) {
        taskService.removeMember(taskId, memberId);
        return SuccessResponse.ok("담당자 제거 완료");
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

    @GetMapping("/projects/{projectId}/tasks")
    @Operation(summary = "내 업무 조회", description = "내가 담당하고 있는 진행 중인 업무와 Todo 현황을 조회합니다.")
    public SuccessResponse<TaskResponseDTO.MyTasksResponse> getMyTasks(
            @CurrentMember Member currentMember,
            @PathVariable Long projectId
    ) {
        return SuccessResponse.ok(taskService.getMyInProgressTasks(currentMember, projectId));
    }

}
