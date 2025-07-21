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
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "url", ignore = true)
    @Mapping(target = "isMain", ignore = true)
    Image toEntity(ImageRequest request);

    ImageResponse toResponse(Image image);

    List<ImageResponse> toResponseList(List<Image> images);
}