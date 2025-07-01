package we.LiteBoard.domain.category.repository;

import we.LiteBoard.domain.category.entity.Category;

import java.util.List;

public interface CategoryRepository {
    Category save(Category category);
    Category getById(Long id);
    List<Category> findAllByProjectId(Long projectId);
    void deleteById(Long id);
}
