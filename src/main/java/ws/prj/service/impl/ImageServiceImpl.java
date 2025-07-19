package ws.prj.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ws.prj.dto.request.ImageRequest;
import ws.prj.dto.response.ImageResponse;
import ws.prj.entity.Image;
import ws.prj.entity.Product;
import ws.prj.mapper.ImageMapper;
import ws.prj.repository.ImageRepositoryDAO;
import ws.prj.repository.ProductRepositoryDAO;
import ws.prj.service.ImageService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageServiceImpl implements ImageService {

    ImageRepositoryDAO imageRepository;
    ProductRepositoryDAO productRepository;
    ImageMapper imageMapper;

    @Override
    public ImageResponse create(ImageRequest request) {
        // 1. Lấy Product từ DB (bắt buộc nếu dùng JPA đúng cách)
        Product product = productRepository.findById(request.getId_product())
                .orElseThrow(() -> new RuntimeException("Product not found")); // Hoặc tạo custom exception

        // 2. Convert request -> Entity
        Image image = imageMapper.toEntity(request);

        // 3. Gán product đã lấy vào image
        image.setProduct(product);

        // 4. Lưu và trả về response
        Image savedImage = imageRepository.save(image);
        return imageMapper.toResponse(savedImage);
    }

    @Override
    public List<ImageResponse> findByProductId(UUID productId) {
        List<Image> images = imageRepository.findByProductId(productId);
        return imageMapper.toResponseList(images);
    }

    @Override
    public void delete(UUID id) {
        imageRepository.deleteById(id);
    }

    private final DataSource dataSource;

    @PostConstruct
    public void printDbUrl() throws SQLException {
        System.out.println("✅ DB URL đang kết nối: " + dataSource.getConnection().getMetaData().getURL());
    }
}
