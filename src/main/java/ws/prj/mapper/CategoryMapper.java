package ws.prj.mapper;

import org.mapstruct.Mapper;
import ws.prj.dto.request.CategoryRequest;
import ws.prj.dto.response.CategoryResponse;
import ws.prj.entity.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryRequest request);

    CategoryResponse toResponse(Category category);

    List<CategoryResponse> toResponseList(List<Category> categories);
}

