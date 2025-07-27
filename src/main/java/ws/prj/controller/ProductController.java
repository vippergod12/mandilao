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
        return ApiResponse.<List<ProductResponse>>builder()
                .result(products)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getById(@PathVariable UUID id) {
        ProductResponse product = service.findById(id);
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
        System.out.println("Files received: " + images.length);
//        for (MultipartFile img : images) {
//            System.out.println("File: " + img.getOriginalFilename());
//        }

        System.out.println("Product JSON: " + productJson);
        System.out.println("Files received: " + images.length);
        for (MultipartFile img : images) {
            if (img != null) {
                System.out.println("----- Ảnh nhận được -----");
                System.out.println("Tên file      : " + img.getOriginalFilename());
                System.out.println("Loại MIME     : " + img.getContentType());
            } else {
                System.out.println("Một phần tử ảnh bị null");
            }
        }

        // Parse chuỗi JSON thành đối tượng
        ProductRequest productRequest = objectMapper.readValue(productJson, ProductRequest.class);

        ProductResponse response = service.create(productRequest, images);

        return ApiResponse.<ProductResponse>builder()
                .result(response)
                .message("Tao san pham thanh cong")
                .build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ProductResponse> update(
            @PathVariable UUID id,
            @RequestPart("product") String productJson,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) throws JsonProcessingException {
        ProductRequest productRequest = objectMapper.readValue(productJson, ProductRequest.class);
        ProductResponse response = service.update(id, productRequest);
        return ApiResponse.<ProductResponse>builder()
                .result(response)
                .message("Cap nhat san pham thanh cong")
                .build();
    }


    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        service.delete(id);
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
