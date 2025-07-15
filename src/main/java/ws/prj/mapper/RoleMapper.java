package ws.prj.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ws.prj.dto.request.RoleRequest;
import ws.prj.dto.response.RoleResponse;
import ws.prj.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
