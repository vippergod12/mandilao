package ws.prj.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PaymentResponse {
    UUID id;
    UUID id_order;
    String name_table;
    String name_admin;
    String name_user;
//    List<OrderDetailResponse> orderDetailResponseList;
    double totailPrice;
    LocalDateTime payment_time;
}
