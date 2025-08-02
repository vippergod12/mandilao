package ws.prj.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
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

    @Autowired
    private RestClient.Builder builder;

//    @GetMapping
//    public ApiResponse<List<ProductResponse>> getAll() {
//        List<ProductResponse> products = service.findAll();
////        log.info("Fetched {} products", products.size());
//        return ApiResponse.<List<ProductResponse>>builder()
//                .result(products)
//                .build();
//    }
    @GetMapping
    public ApiResponse<Page<ProductResponse>> getAll(@RequestParam("page") int page,
                                                     @RequestParam("size") int size,
                                                     @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
                                                     @RequestParam(name = "direction", defaultValue = "asc")String direction,
                                                     @RequestParam(name = "categoryId", required = false) UUID categoryId){

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<ProductResponse> products = service.findAll(pageRequest,categoryId);

//        http://localhost:8080/identity/product?page=0&size=10&sortBy=name&direction=desc
//        http://localhost:8080/identity/product?page=0&size=10&sortBy=price&direction=desc
        return ApiResponse.<Page<ProductResponse>>builder().result(products).build();
    }


    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getById(@PathVariable UUID id) {
        ProductResponse product = service.findById(id);
//        log.info("Fetched product with ID: {}, images: {}", id, product.getImages());
        return ApiResponse.<ProductResponse>builder()
                .result(product)
                .build();
    }
    @GetMapping("/search")
    public ApiResponse<List<ProductResponse>> search(@RequestParam(name="name") String name) {
        List<ProductResponse> productResponseList = service.searchByName(name);
        return ApiResponse.<List<ProductResponse>>builder()
                .result(productResponseList)
                .build();
//        http://localhost:8080/identity/product/search?name=iphone
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
