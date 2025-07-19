package ws.prj.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ws.prj.dto.request.ProductRequest;
import ws.prj.dto.response.ProductResponse;
import ws.prj.entity.Product;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "id_category", target = "category.id")
    Product toEntity(ProductRequest request);

    @Mapping(source = "category.id", target = "id_category")
    ProductResponse toResponse(Product product);

    List<ProductResponse> toResponseList(List<Product> products);
}
