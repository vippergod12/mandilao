package ws.prj.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ws.prj.dto.request.ApiResponse;
import ws.prj.dto.request.TableTypeRequest;
import ws.prj.dto.response.TableTypeResponse;
import ws.prj.service.TableTypeService;

import java.util.List;

@RestController
@RequestMapping("/table-types")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TableTypeController {

    TableTypeService service;


    @GetMapping
    public ApiResponse<List<TableTypeResponse>> getAll() {
        List<TableTypeResponse> types = service.findAll();
        return ApiResponse.<List<TableTypeResponse>>builder()
                .result(types)
                .message("Lấy danh sách loại bàn thành công")
                .build();
    }


    @GetMapping("/{id}")
    public ApiResponse<TableTypeResponse> getById(@PathVariable Long id) {
        TableTypeResponse type = service.findById(id);
        return ApiResponse.<TableTypeResponse>builder()
                .result(type)
                .message("Lấy loại bàn thành công")
                .build();
    }


    @PostMapping
    public ApiResponse<TableTypeResponse> create(@RequestBody TableTypeRequest request) {
        TableTypeResponse response = service.create(request);
        log.info("Tạo loại bàn: {}", response.getId());
        return ApiResponse.<TableTypeResponse>builder()
                .result(response)
                .message("Tạo loại bàn thành công")
                .build();
    }


    @PutMapping("/{id}")
    public ApiResponse<TableTypeResponse> update(@PathVariable Long id, @RequestBody TableTypeRequest request) {
        TableTypeResponse response = service.update(id, request);
        log.info("Cập nhật loại bàn ID: {}", id);
        return ApiResponse.<TableTypeResponse>builder()
                .result(response)
                .message("Cập nhật loại bàn thành công")
                .build();
    }


    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        log.info("Xóa loại bàn ID: {}", id);
        return ApiResponse.<Void>builder()
                .message("Xóa loại bàn thành công")
                .build();
    }
}
