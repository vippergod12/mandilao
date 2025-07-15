package ws.prj.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ws.prj.dto.request.ProductRequest;
import ws.prj.dto.response.ProductResponse;
import ws.prj.entity.Product;
import ws.prj.mapper.ProductMapper;
import ws.prj.repository.ProductRepositoryDAO;
import ws.prj.service.ProductService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    ProductRepositoryDAO repo;
    ProductMapper mapper;

    @Override
    public List<ProductResponse> findAll() {
        List<Product> entities = repo.findAll();
        return mapper.toResponseList(entities);
    }

    @Override
    public ProductResponse findById(UUID id) {
        Product found = repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return mapper.toResponse(found);
    }

    @Override
    public ProductResponse create(ProductRequest request) {
        Product saved = repo.save(mapper.toEntity(request));
        return mapper.toResponse(saved);
    }

    @Override
    public ProductResponse update(UUID ID, ProductRequest request) {
        Product existing = repo.findById(ID).orElseThrow(() -> new RuntimeException("Product not found"));
        existing.setName(request.getName());
        return mapper.toResponse(repo.save(existing));
    }

    @Override
    public void delete(UUID id) {
        repo.deleteById(id);
    }

}
