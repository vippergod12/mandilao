package ws.prj.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ws.prj.dto.request.ApiResponse;
import ws.prj.dto.request.ImageRequest;
import ws.prj.dto.response.ImageResponse;
import ws.prj.service.ImageService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ImageController {

    ImageService imageService;

    @GetMapping(value = "/uploads/{filename}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path uploadDir = Paths.get("uploads"); // đường dẫn thư mục lưu ảnh
            Path filePath = uploadDir.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                String contentType = Files.probeContentType(filePath);
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Upload ảnh cho sản phẩm.
     * Gửi dạng: multipart/form-data
     *
     * Params:
     * - file: ảnh
     * - isMain: true/false
     * - productId: UUID của product
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<ImageResponse> create(
            @RequestPart("file") MultipartFile file,
            @RequestParam("isMain") boolean isMain,
            @RequestParam("productId") UUID productId
    ) {
        ImageRequest request = ImageRequest.builder()
                .file(file)
                .isMain(isMain)
                .build();

        return ApiResponse.<ImageResponse>builder()
                .message("Image uploaded")
                .result(imageService.create(request, productId))
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
