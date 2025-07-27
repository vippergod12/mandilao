package ws.prj.service;


import ws.prj.dto.request.TableTypeRequest;
import ws.prj.dto.response.TableTypeResponse;

import java.util.List;

public interface TableTypeService {
    List<TableTypeResponse> findAll();
    TableTypeResponse findById(Long id);
    TableTypeResponse create(TableTypeRequest request);
    TableTypeResponse update(Long id, TableTypeRequest request);
    void delete(Long id);
}
