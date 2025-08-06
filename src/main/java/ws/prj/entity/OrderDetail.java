package ws.prj.entity;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    int quantity;
    double price;
    Date createdAt;
    @ManyToOne
    @JoinColumn(name = "id_order")
    Orders orders;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_product")
    Product product;
}
