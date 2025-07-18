package ws.prj.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ws.prj.dto.request.ApiResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<String> uploadImage(@RequestPart("file") MultipartFile file,
                                           @RequestParam("id_product") UUID idProduct) {
        try {
            if (file == null || file.isEmpty()) {
                return ApiResponse.<String>builder()
                        .code(400)
                        .message("Không có file được tải lên")
                        .build();
            }

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + filename);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            String fileUrl = "http://localhost:8080/uploads/" + filename;

            return ApiResponse.<String>builder()
                    .message("Upload thành công")
                    .result(fileUrl)
                    .build();

        } catch (IOException e) {
            return ApiResponse.<String>builder()
                    .code(500)
                    .message("Upload thất bại: " + e.getMessage())
                    .build();
        }
    }
}
