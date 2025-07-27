package ws.prj.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ws.prj.dto.request.ApiResponse;
import ws.prj.dto.request.TableRequest;
import ws.prj.dto.response.TableResponse;
import ws.prj.repository.TableRespositoryDAO;
import ws.prj.service.TablesService;

import java.util.List;

@RestController
@RequestMapping("/tables")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TablesController {

    TablesService service;
    TableRespositoryDAO tableRespositoryDAO;

    @GetMapping
    public ApiResponse<List<TableResponse>> getAll() {
        List<TableResponse> tables = service.findAll();
        return ApiResponse.<List<TableResponse>>builder()
                .result(tables)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<TableResponse> getById(@PathVariable Long id) {
        TableResponse table = service.findById(id);
        log.info("Fetched table with ID: {}", id);
        return ApiResponse.<TableResponse>builder()
                .result(table)
                .build();
    }

    @PostMapping
    public ApiResponse<TableResponse> create(@RequestBody TableRequest request) {
        TableResponse response = service.create(request);
        log.info("Created table: {}", response.getId());
        return ApiResponse.<TableResponse>builder()
                .result(response)
                .message("Tạo bàn thành công")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<TableResponse> update(@PathVariable Long id, @RequestBody TableRequest request) {
        TableResponse response = service.update(id, request);
        log.info("Updated table with ID: {}", id);
        return ApiResponse.<TableResponse>builder()
                .result(response)
                .message("Cập nhật bàn thành công")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        log.info("Deleted table with ID: {}", id);
        return ApiResponse.<Void>builder()
                .message("Xóa bàn thành công")
                .build();
    }
}
