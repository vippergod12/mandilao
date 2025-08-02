package ws.prj.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ws.prj.dto.response.ProductResponse;
import ws.prj.entity.Product;


import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepositoryDAO extends JpaRepository<Product, UUID> {
    Product findByName(String name);
    Page<Product> findByCategoryId(UUID categoryId, Pageable pageable);
    List<Product> findByNameContainingIgnoreCase(@Param("name") String name);
}
