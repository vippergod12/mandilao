package ws.prj.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder

public class OrderReponse {
    UUID id;
    String status;
    List<OrderDetailResponse> orderDetails;
    Date createdAt;
    Date updatedAt;
}
