package ws.prj.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetail {
    @Id
    UUID id;
    int quantity;
    double price;
    Date createdAt;
    @ManyToOne
    @JoinColumn(name = "id_order")
    Orders orders;
    @ManyToOne
    @JoinColumn(name = "id_product")
    Product product;
}
