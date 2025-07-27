package ws.prj.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableRequest {
    Long id;
    String name;
    String status;
    Long tableTypeId;
}
