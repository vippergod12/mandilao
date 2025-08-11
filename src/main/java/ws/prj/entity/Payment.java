package ws.prj.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @Column(name = "name_admin")
    String name_admin;

    @Column(name = "name_user")
    String name_user;

    double totailPrice;
    LocalDateTime payment_time;

    @ManyToOne
    @JoinColumn(name = "id_user")
    User user;

    @ManyToOne
    @JoinColumn(name = "id_table")
    Tables tables;

    @OneToOne
    @JoinColumn(name = "id_order")
    Orders orders;

}
