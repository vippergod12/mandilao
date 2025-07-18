package ws.prj.dto.request;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ws.prj.validator.DobConstraint;

import java.time.LocalDate;

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
     @DobConstraint(min = 10, message = "INVALID_DOB")
     LocalDate dob;
     String fullName;

}
