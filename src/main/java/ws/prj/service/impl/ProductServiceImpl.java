package ws.prj.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.prj.dto.request.ProductRequest;
import ws.prj.dto.response.ImageResponse;
import ws.prj.dto.response.ProductResponse;
import ws.prj.entity.Category;
import ws.prj.entity.Image;
import ws.prj.entity.Product;
import ws.prj.mapper.ProductMapper;
import ws.prj.repository.CategoryRepositoryDAO;
import ws.prj.repository.ProductRepositoryDAO;
import ws.prj.service.ProductService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    ProductRepositoryDAO repo;

    @Qualifier("productMapperImpl")
    @Autowired
    ProductMapper mapper;

    CategoryRepositoryDAO categoryRepo;

    @Override
    public List<ProductResponse> findAll() {
        List<Product> entities = repo.findAll();
        return mapper.toResponseList(entities);
    }

    @Override
    public ProductResponse findById(UUID id) {
        Product found = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapper.toResponse(found);
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request, MultipartFile[] images) {
        // 1. Tìm Category
        Category category = categoryRepo.findById(request.getId_category())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // 2. Tạo Product entity
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .description(request.getDescription())
                .category(category)
                .build();

        // 3. Tạo thư mục "uploads" cạnh pom.xml nếu chưa tồn tại
        Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads"); // đổi ở đây nè
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Không thể tạo thư mục uploads", e);
        }

        // 4. Xử lý ảnh
        List<Image> imageList = new ArrayList<>();
        List<Boolean> isMainList = request.getIsMain();

        for (int i = 0; i < images.length; i++) {
            MultipartFile file = images[i];
            String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
            String storedFilename = UUID.randomUUID() + "_" + originalFilename;

            // ✅ Lưu file vào thư mục uploads (KHÔNG có /identity)
            Path filePath = uploadDir.resolve(storedFilename);
            try {
                Files.write(filePath, file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Không thể lưu file ảnh: " + storedFilename, e);
            }

            // ✅ Đường dẫn URL có prefix /identity/uploads/
            String publicUrl = "http://localhost:8080/identity/uploads/" + storedFilename;

            // Xác định ảnh chính
            boolean isMain = false;
            if (isMainList != null && i < isMainList.size()) {
                isMain = Boolean.TRUE.equals(isMainList.get(i));
            } else if (i == 0) {
                isMain = true;
            }

            Image image = Image.builder()
                    .url("http://localhost:8080/identity/uploads/" + storedFilename)
                    .isMain(isMain)
                    .product(product)
                    .build();

            imageList.add(image);
        }


        // 5. Gán ảnh vào sản phẩm rồi lưu
        product.setImages(imageList);
        product = repo.save(product);

        // 6. Trả về DTO
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .id_category(product.getCategory().getId())
                .images(product.getImages().stream()
                        .map(img -> ImageResponse.builder()
                                .id(img.getId())
                                .url(img.getUrl())
                                .isMain(img.isMain())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public ProductResponse update(UUID id, ProductRequest request) {
        Product existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existing.setName(request.getName());
        existing.setPrice(request.getPrice());
        existing.setQuantity(request.getQuantity());
        existing.setDescription(request.getDescription());

        if (request.getId_category() != null) {
            Category category = categoryRepo.findById(request.getId_category())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existing.setCategory(category);
        }

        return mapper.toResponse(repo.save(existing));
    }

    @Override
    public void delete(UUID id) {
        repo.deleteById(id);
    }
}