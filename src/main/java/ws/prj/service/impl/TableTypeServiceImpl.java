package ws.prj.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.stereotype.Service;
import ws.prj.dto.request.TableTypeRequest;
import ws.prj.dto.response.TableTypeResponse;
import ws.prj.entity.TableType;
import ws.prj.repository.TableTypeResponsitoryDAO;
import ws.prj.service.TableTypeService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TableTypeServiceImpl implements TableTypeService {

    TableTypeResponsitoryDAO tableTypeRepository;

    @Override
    public List<TableTypeResponse> findAll() {
        return tableTypeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TableTypeResponse findById(Long id) {
        TableType tableType = tableTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TableType không tồn tại"));
        return mapToResponse(tableType);
    }

    @Override
    public TableTypeResponse create(TableTypeRequest request) {
        TableType tableType = TableType.builder()
                .name(request.getName())
                .build();
        return mapToResponse(tableTypeRepository.save(tableType));
    }

    @Override
    public TableTypeResponse update(Long id, TableTypeRequest request) {
        TableType existing = tableTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TableType không tồn tại"));

        existing.setName(request.getName());
        return mapToResponse(tableTypeRepository.save(existing));
    }

    @Override
    public void delete(Long id) {
        if (!tableTypeRepository.existsById(id)) {
            throw new RuntimeException("TableType không tồn tại");
        }
        tableTypeRepository.deleteById(id);
    }

    // ✅ Helper: Convert Entity -> Response DTO
    private TableTypeResponse mapToResponse(TableType tableType) {
        return TableTypeResponse.builder()
                .id(tableType.getId())
                .name(tableType.getName())
                .build();
    }
}
