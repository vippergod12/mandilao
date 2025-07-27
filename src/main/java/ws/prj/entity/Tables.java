package ws.prj.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tables {
    @Id
    UUID id;
    String name;
    String status;
    @OneToMany(mappedBy = "tables")
    List<Orders> orders;

    @ManyToOne
    @JoinColumn(name = "id_tableType")
    TableType tableType;
}
