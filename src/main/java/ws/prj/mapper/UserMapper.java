package ws.prj.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ws.prj.dto.request.UserCreationRequest;
import ws.prj.dto.request.UserUpdateRequest;
import ws.prj.dto.response.UserResponse;
import ws.prj.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "dob", target = "dob")
    User toUser(UserCreationRequest request);

    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "dob", target = "dob")
    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
