package ws.prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ws.prj.entity.Category;

import java.util.UUID;

@Repository
public interface CategoryRepositoryDAO extends JpaRepository<Category, UUID> {
    Category findByName(String name);
}
