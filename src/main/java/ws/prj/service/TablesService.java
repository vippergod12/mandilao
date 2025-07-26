package ws.prj.service;

import org.springframework.web.multipart.MultipartFile;
import ws.prj.dto.request.TableRequest;
import ws.prj.dto.response.TableResponse;

import java.util.List;

public interface TablesService {
    List<TableResponse> findAll();
    TableResponse findById(Long id);
    TableResponse create(TableRequest request);
    TableResponse update(Long id, TableRequest request);
    void delete(Long id);
}
