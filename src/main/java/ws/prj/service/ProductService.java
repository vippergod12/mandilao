package ws.prj.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ws.prj.dto.request.ProductRequest;
import ws.prj.dto.response.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<ProductResponse> findAll();
    ProductResponse findById(UUID id);
//    List<ProductResponse> findByName(String name);
    List<ProductResponse> searchByName(String name);
    ProductResponse create(ProductRequest request, MultipartFile[] images);
    ProductResponse update(UUID ID,ProductRequest request);
    void delete(UUID id);

     Page<ProductResponse> findAll(Pageable pageable);
}
