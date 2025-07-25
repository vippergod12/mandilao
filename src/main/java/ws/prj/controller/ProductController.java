package ws.prj.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ws.prj.dto.request.ApiResponse;
import ws.prj.dto.request.ProductRequest;
import ws.prj.dto.response.ProductResponse;
import ws.prj.entity.Product;
import ws.prj.repository.ProductRepositoryDAO;
import ws.prj.service.ProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductController {
    ProductService service;
    ProductRepositoryDAO productRepositoryDAO;

    @GetMapping
    public ApiResponse<List<ProductResponse>> getAll() {
        List<ProductResponse> products = service.findAll();
//        log.info("Fetched {} products", products.size());
        return ApiResponse.<List<ProductResponse>>builder()
                .result(products)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getById(@PathVariable UUID id) {
        ProductResponse product = service.findById(id);
//        log.info("Fetched product with ID: {}, images: {}", id, product.getImages());
        return ApiResponse.<ProductResponse>builder()
                .result(product)
                .build();
    }

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductResponse> createProduct(
            @RequestPart("product") String productJson,
            @RequestPart("images") MultipartFile[] images
    ) throws JsonProcessingException {
        log.info("Creating product with raw json: {}", productJson);

        // Parse chuỗi JSON thành đối tượng
        ProductRequest productRequest = objectMapper.readValue(productJson, ProductRequest.class);

        ProductResponse response = service.create(productRequest, images);

        return ApiResponse.<ProductResponse>builder()
                .result(response)
                .message("Tao san pham thanh cong")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> update(@PathVariable UUID id, @RequestBody ProductRequest request) {
        ProductResponse response = service.update(id, request);
        log.info("Updated product with ID: {}", id);
        return ApiResponse.<ProductResponse>builder()
                .result(response)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        log.info("Deleted product with ID: {}", id);
        return ApiResponse.<Void>builder()
                .message("Product deleted")
                .build();
    }

    @GetMapping("/test-db")
    public List<Product> testDb() {
        List<Product> products = productRepositoryDAO.findAll();
        log.info("Fetched {} products from test-db", products.size());
        return products;
    }
}
