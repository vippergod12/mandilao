package ws.prj.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ws.prj.dto.request.ImageRequest;
import ws.prj.dto.response.ImageResponse;
import ws.prj.entity.Image;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    @Mapping(source = "id_product", target = "product.id")
    Image toEntity(ImageRequest request);

    @Mapping(source = "product.id", target = "id_product")
    ImageResponse toResponse(Image image);

    List<ImageResponse> toResponseList(List<Image> images);
}
