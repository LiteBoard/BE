package we.LiteBoard.domain.category.service;

import we.LiteBoard.domain.category.dto.CategoryRequestDTO;
import we.LiteBoard.domain.category.dto.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {
    Long create(Long projectId, CategoryRequestDTO.Upsert request);
    Long update(Long categoryId, CategoryRequestDTO.Upsert request);
    List<CategoryResponseDTO.Detail> getAllByProjectId(Long projectId);
    void deleteById(Long categoryId);
}
