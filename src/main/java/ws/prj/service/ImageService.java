package ws.prj.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import ws.prj.dto.request.ImageRequest;
import ws.prj.dto.response.ImageResponse;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface ImageService {
    ImageResponse create(ImageRequest request, UUID productId);
    List<ImageResponse> findByProductId(UUID productId);
    void delete(UUID id);
}
