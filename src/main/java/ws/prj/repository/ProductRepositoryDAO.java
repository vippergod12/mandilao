package ws.prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ws.prj.entity.Product;

import java.util.UUID;

@Repository
public interface ProductRepositoryDAO extends JpaRepository<Product, UUID> {
    Product findByName(String name);
}
