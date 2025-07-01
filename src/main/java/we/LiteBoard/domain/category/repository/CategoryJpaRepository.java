package we.LiteBoard.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import we.LiteBoard.domain.category.entity.Category;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
}
