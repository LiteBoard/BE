package we.LiteBoard.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import we.LiteBoard.domain.category.entity.Category;

import java.util.List;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByProjectId(Long projectId);
}
