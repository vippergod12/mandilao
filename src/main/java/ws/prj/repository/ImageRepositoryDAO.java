package ws.prj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ws.prj.entity.Image;
import ws.prj.entity.Product;

import java.util.List;
import java.util.UUID;

public interface ImageRepositoryDAO extends JpaRepository<Image, UUID> {
    List<Image> findByProductId(UUID productId);
}
