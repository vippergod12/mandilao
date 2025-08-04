package ws.prj.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ws.prj.dto.request.CategoryRequest;
import ws.prj.dto.response.CategoryResponse;
import ws.prj.entity.Category;
import ws.prj.mapper.CategoryMapper;
import ws.prj.repository.CategoryRepositoryDAO;
import ws.prj.service.CategoryService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {

    CategoryRepositoryDAO repo;

    @Qualifier("categoryMapperImpl")
    CategoryMapper mapper;

    @Override
    public List<CategoryResponse> findAll() {
        List<Category> entities = repo.findAll();
        return mapper.toResponseList(entities);
    }

    @Override
    public CategoryResponse findById(UUID id) {
        Category found = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        return mapper.toResponse(found);
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {
        Category saved = repo.save(mapper.toEntity(request));
        return mapper.toResponse(saved);
    }

    @Override
    public CategoryResponse update(UUID id, CategoryRequest request) {
        Category existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        return mapper.toResponse(repo.save(existing));
    }

    @Override
    public void delete(UUID id) {
        repo.deleteById(id);
    }

}
