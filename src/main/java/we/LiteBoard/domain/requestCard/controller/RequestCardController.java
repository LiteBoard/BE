package we.LiteBoard.domain.requestCard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import we.LiteBoard.domain.member.entity.Member;
import we.LiteBoard.domain.requestCard.dto.RequestCardRequestDTO;
import we.LiteBoard.domain.requestCard.dto.RequestCardResponseDTO;
import we.LiteBoard.domain.requestCard.service.RequestCardService;
import we.LiteBoard.global.common.annotation.CurrentMember;
import we.LiteBoard.global.response.SuccessResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "업무 요청")
@RequestMapping("/api/v1")
public class RequestCardController {

    private final RequestCardService requestCardService;

    @PostMapping("/tasks/{taskId}/request-cards")
    @Operation(summary = "업무 요청 생성", description = "특정 업무(Task)에 대해 특정 사용자에게 요청 메시지를 보냅니다.")
    public SuccessResponse<RequestCardResponseDTO.Upsert> createRequestCard(
            @CurrentMember Member currentMember,
            @PathVariable Long taskId,
            @RequestBody RequestCardRequestDTO.Create request
    ) {
        return SuccessResponse.ok(requestCardService.create(currentMember, taskId, request));
    }

    @PatchMapping("/request-cards/{requestCardId}")
    @Operation(summary = "업무 요청 수정", description = "업무 요청의 내용을 수정합니다.")
    public SuccessResponse<RequestCardResponseDTO.Upsert> updateRequestCard(
            @PathVariable Long requestCardId,
            @RequestBody RequestCardRequestDTO.Update request
    ) {
        return SuccessResponse.ok(requestCardService.update(requestCardId, request));
    }

    @GetMapping("/tasks/{taskId}/request-cards")
    @Operation(summary = "특정 업무의 업무 요청 전체 조회", description = "특정 업무에 속한 모든 요청 메시지 및 할 일 목록을 조회합니다.")
    public SuccessResponse<List<RequestCardResponseDTO.Detail>> getRequestCardsByTask(
            @PathVariable Long taskId
    ) {
        List<RequestCardResponseDTO.Detail> result = requestCardService.getAllByTaskId(taskId);
        return SuccessResponse.ok(result);
    }

    @DeleteMapping("/request-cards/{requestCardId}")
    @Operation(summary = "요청 삭제", description = "업무 요청을 삭제합니다.")
    public SuccessResponse<String> deleteRequestCard(@PathVariable Long requestCardId) {
        requestCardService.deleteById(requestCardId);
        return SuccessResponse.ok("업무 요청이 삭제되었습니다.");
    }
}
