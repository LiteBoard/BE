package we.LiteBoard.domain.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import we.LiteBoard.domain.category.dto.CategoryRequestDTO;
import we.LiteBoard.domain.category.dto.CategoryResponseDTO;
import we.LiteBoard.domain.category.entity.Category;
import we.LiteBoard.domain.category.repository.CategoryRepository;
import we.LiteBoard.domain.project.entity.Project;
import we.LiteBoard.domain.project.repository.ProjectRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProjectRepository projectRepository;

    /**
     * 카테고리 생성
     * @param projectId 카테고리를 생성할 프로젝트 ID
     * @param request 생성 요청 DTO
     * @return 생성된 카테고리 ID
     */
    @Override
    @Transactional
    public Long create(Long projectId, CategoryRequestDTO.Upsert request) {
        Project project = projectRepository.getById(projectId);

        Category category = Category.builder()
                .title(request.title())
                .build();

        project.addCategory(category);

        return categoryRepository.save(category).getId();
    }

    /**
     * 카테고리 수정
     * @param categoryId 수정할 카테고리 ID
     * @param request 수정 요청 DTO
     * @return 수정된 카테고리 ID
     */
    @Override
    @Transactional
    public Long update(Long categoryId, CategoryRequestDTO.Upsert request) {
        Category category = categoryRepository.getById(categoryId);
        category.updateTitle(request.title());
        return category.getId();

    }

    /**
     * 프로젝트를 기준으로 카테고리 목록 조회
     * @param projectId 조회할 프로젝트 ID
     * @return 해당 프로젝트에 속한 카테고리 전체 반환
     */
    @Override
    public List<CategoryResponseDTO.Detail> getAllByProjectId(Long projectId) {
        projectRepository.getById(projectId);

        return categoryRepository.findAllByProjectId(projectId).stream()
                .map(CategoryResponseDTO.Detail::from)
                .toList();
    }

    /**
     * 카테고리 삭제
     * @param categoryId 삭제할 카테고리 ID
     */
    @Override
    @Transactional
    public void deleteById(Long categoryId) {
        categoryRepository.getById(categoryId);
        categoryRepository.deleteById(categoryId);
    }
}
