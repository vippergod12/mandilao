package ws.prj.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
    private final ProductRepositoryDAO productRepositoryDAO;

    @GetMapping
    public ApiResponse<List<ProductResponse>> getAll() {
        return ApiResponse.<List<ProductResponse>>builder()
                .result(service.findAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getById(@PathVariable UUID id) {
        return ApiResponse.<ProductResponse>builder()
                .result(service.findById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<ProductResponse> create(@RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(service.create(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> update(@PathVariable UUID id, @RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(service.update(id, request))
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
        return productRepositoryDAO.findAll();
    }
}
