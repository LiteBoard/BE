package we.LiteBoard.domain.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import we.LiteBoard.domain.project.dto.ProjectRequestDTO;
import we.LiteBoard.domain.project.dto.ProjectResponseDTO;
import we.LiteBoard.domain.project.service.ProjectService;
import we.LiteBoard.global.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "프로젝트")
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @Operation(summary = "프로젝트 생성 API", description = "프로젝트명을 입력받아 프로젝트를 생성합니다.")
    public SuccessResponse<Long> create(@RequestBody @Valid ProjectRequestDTO.Create request) {
        Long projectId = projectService.create(request);
        return SuccessResponse.ok(projectId);
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "프로젝트 단건 조회 API", description = "프로젝트 ID로 단건 조회합니다.")
    public SuccessResponse<ProjectResponseDTO.Detail> getById(@PathVariable Long projectId) {
        ProjectResponseDTO.Detail result = projectService.getById(projectId);
        return SuccessResponse.ok(result);
    }
}
