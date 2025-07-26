package ws.prj.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ws.prj.dto.request.TableRequest;
import ws.prj.dto.response.TableResponse;
import ws.prj.entity.TableType;
import ws.prj.entity.Tables;
import ws.prj.repository.TableTypeResponsitoryDAO;
import ws.prj.repository.TableRespositoryDAO;
import ws.prj.service.TablesService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TablesServiceImpl implements TablesService {

    private final TableRespositoryDAO tablesRepository;
    private final TableTypeResponsitoryDAO tableTypeRepository;

    @Override
    public List<TableResponse> findAll() {
        return tablesRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TableResponse findById(Long id) {
        Tables table = tablesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found"));
        return mapToResponse(table);
    }

    @Override
    public TableResponse create(TableRequest request) {
        TableType tableType = tableTypeRepository.findById(request.getTableTypeId())
                .orElseThrow(() -> new RuntimeException("TableType not found"));

        Tables table = Tables.builder()
                .name(request.getName())
                .status(request.getStatus())
                .tableType(tableType)
                .build();

        return mapToResponse(tablesRepository.save(table));
    }

    @Override
    public TableResponse update(Long id, TableRequest request) {
        Tables existing = tablesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found"));

        existing.setName(request.getName());
        existing.setStatus(request.getStatus());

        if (request.getTableTypeId() != null) {
            TableType tableType = tableTypeRepository.findById(request.getTableTypeId())
                    .orElseThrow(() -> new RuntimeException("TableType not found"));
            existing.setTableType(tableType);
        }

        return mapToResponse(tablesRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        if (!tablesRepository.existsById(id)) {
            throw new RuntimeException("Table not found");
        }
        tablesRepository.deleteById(id);
    }

    private TableResponse mapToResponse(Tables table) {
        return TableResponse.builder()
                .id(table.getId())
                .name(table.getName())
                .status(table.getStatus())
                .tableTypeName(table.getTableType() != null ? table.getTableType().getName() : null)
                .build();
    }

}
