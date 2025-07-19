package ws.prj.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Image {
    @Id
    @GeneratedValue
    UUID id;

    String url;

    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    Product product;

    // Optional: là ảnh chính không?
    boolean isMain;
}
