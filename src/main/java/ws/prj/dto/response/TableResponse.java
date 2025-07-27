package ws.prj.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableResponse {
    Long id;
    String name;
    String status;
    String tableTypeName;
}
