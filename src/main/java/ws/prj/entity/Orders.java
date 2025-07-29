package ws.prj.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Orders {
    @Id
    UUID id;
    Date createdAt;
    Date updatedAt;
    String status;
    @ManyToOne
    @JoinColumn(name = "id_table")
    Tables tables;

    @ManyToOne
    @JoinColumn(name = "id_user")
    User user;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderDetail> orderDetails;
}
