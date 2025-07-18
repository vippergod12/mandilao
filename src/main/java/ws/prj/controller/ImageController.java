package ws.prj.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ws.prj.dto.request.ApiResponse;
import ws.prj.dto.request.ImageRequest;
import ws.prj.dto.response.ImageResponse;
import ws.prj.service.ImageService;

import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ImageController {

    ImageService imageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ImageResponse> create(@ModelAttribute ImageRequest request) {
        return ApiResponse.<ImageResponse>builder()
                .message("Image uploaded")
                .result(imageService.create(request))
                .build();
    }

    @GetMapping(value = "/product/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<List<ImageResponse>> getByProductId(@PathVariable UUID productId) {
        return ApiResponse.<List<ImageResponse>>builder()
                .result(imageService.findByProductId(productId))
                .build();
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        imageService.delete(id);
        return ApiResponse.<Void>builder()
                .message("Image deleted")
                .build();
    }
}
