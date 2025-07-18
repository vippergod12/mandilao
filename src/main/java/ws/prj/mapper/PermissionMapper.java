package ws.prj.mapper;

import org.mapstruct.Mapper;
import ws.prj.dto.request.PermissionRequest;
import ws.prj.dto.response.PermissionResponse;
import ws.prj.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission  toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
