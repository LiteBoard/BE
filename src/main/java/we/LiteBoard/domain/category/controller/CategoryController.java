package we.LiteBoard.domain.category.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import we.LiteBoard.domain.category.dto.CategoryRequestDTO;
import we.LiteBoard.domain.category.dto.CategoryResponseDTO;
import we.LiteBoard.domain.category.service.CategoryService;
import we.LiteBoard.global.response.SuccessResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "카테고리")
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/project/{projectId}")
    @Operation(summary = "카테고리 생성", description = "프로젝트에 카테고리를 생성합니다.")
    public SuccessResponse<CategoryResponseDTO.Upsert> create(
            @PathVariable Long projectId,
            @RequestBody @Valid CategoryRequestDTO.Upsert request
    ) {
        Long categoryId = categoryService.create(projectId, request);
        return SuccessResponse.ok(CategoryResponseDTO.Upsert.from(categoryId));
    }

    @PatchMapping("/{categoryId}")
    @Operation(summary = "카테고리 수정", description = "카테고리명을 수정합니다.")
    public SuccessResponse<CategoryResponseDTO.Upsert> update(
            @PathVariable Long categoryId,
            @RequestBody @Valid CategoryRequestDTO.Upsert request
    ) {
        Long id = categoryService.update(categoryId, request);
        return SuccessResponse.ok(CategoryResponseDTO.Upsert.from(id));
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "카테고리 목록 조회", description = "프로젝트에 속한 카테고리 목록을 반환합니다.")
    public SuccessResponse<List<CategoryResponseDTO.Detail>> getAllByProjectId(
            @PathVariable Long projectId
    ) {
        List<CategoryResponseDTO.Detail> result = categoryService.getAllByProjectId(projectId);
        return SuccessResponse.ok(result);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
    public SuccessResponse<String> delete(@PathVariable Long categoryId) {
        categoryService.deleteById(categoryId);
        return SuccessResponse.ok("카테고리 삭제에 성공했습니다.");
    }
}
