package ws.prj.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductResponse {
    UUID id;
    String name;
    String description;
    double price;
    Integer quantity;
    UUID id_category;
}
