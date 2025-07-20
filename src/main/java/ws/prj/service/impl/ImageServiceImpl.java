package ws.prj.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ws.prj.dto.request.ImageRequest;
import ws.prj.dto.response.ImageResponse;
import ws.prj.entity.Image;
import ws.prj.entity.Product;
import ws.prj.mapper.ImageMapper;
import ws.prj.repository.ImageRepositoryDAO;
import ws.prj.repository.ProductRepositoryDAO;
import ws.prj.service.ImageService;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ImageServiceImpl implements ImageService {

    ImageRepositoryDAO imageRepository;
    ProductRepositoryDAO productRepository;
    ImageMapper imageMapper;
    DataSource dataSource;

    @Override
    public ImageResponse create(ImageRequest request, UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 1. Validate file
        MultipartFile file = request.getFile();
        if (file.isEmpty() || file.getOriginalFilename() == null) {
            log.error("Invalid image file");
            throw new RuntimeException("File hình ảnh không hợp lệ");
        }

        // 2. Tạo thư mục uploads nếu chưa tồn tại
        Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads");
        try {
            Files.createDirectories(uploadDir);
            log.info("Created uploads directory at: {}", uploadDir.toAbsolutePath());
        } catch (Exception e) {
            log.error("Không thể tạo thư mục uploads", e);
            throw new RuntimeException("Không thể tạo thư mục uploads", e);
        }

        // 3. Lưu file
        String filename = UUID.randomUUID() + "_" + Objects.requireNonNull(file.getOriginalFilename());
        Path filePath = uploadDir.resolve(filename);
        try {
            Files.write(filePath, file.getBytes());
            log.info("File saved at: {}", filePath.toAbsolutePath());
        } catch (Exception e) {
            log.error("Không thể lưu file ảnh", e);
            throw new RuntimeException("Không thể lưu file ảnh", e);
        }

        // 4. Tạo Image entity
        Image image = imageMapper.toEntity(request);
        image.setProduct(product);
        // Tạo URL động từ request hiện tại
        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/identity/uploads/")
                .path(filename)
                .toUriString();
        image.setUrl(imageUrl);

        // 5. Lưu vào database
        Image savedImage = imageRepository.save(image);
        ImageResponse response = imageMapper.toResponse(savedImage);
        log.info("Image URL in response: {}", response.getUrl());
        return response;
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

    @PostConstruct
    public void printDbUrl() throws SQLException {
        log.info("✅ DB URL đang kết nối: {}", dataSource.getConnection().getMetaData().getURL());
    }
}