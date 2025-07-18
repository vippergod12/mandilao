package ws.prj.service;

import ws.prj.dto.request.CategoryRequest;
import ws.prj.dto.response.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<CategoryResponse> findAll();
    CategoryResponse findById(UUID id);
    CategoryResponse create(CategoryRequest categoryRequest);
    CategoryResponse update(UUID id,CategoryRequest categoryRequest);
    void delete(UUID id);
}
