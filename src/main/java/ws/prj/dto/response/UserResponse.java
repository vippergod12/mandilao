package ws.prj.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ws.prj.entity.Role;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String username;
    String email;
    String phone;
    Set<RoleResponse> roles;
}
