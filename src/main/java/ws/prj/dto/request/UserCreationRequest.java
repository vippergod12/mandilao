package ws.prj.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserCreationRequest {
     String username;
     String password;
     String email;
     String phone;
     boolean enable;
     String image;


}
