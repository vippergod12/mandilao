package ws.prj.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ws.prj.entity.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ChangePassRequest {
    String password;
    String newPass;
    String confirmPass;
}
