package ws.prj.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name="username",unique = true, columnDefinition = "VARCHAR(255) COLLATE SQL_Latin1_General_CP1_CI_AS")
    String username;
    @Column
    String password;
    @Column
    String email;
    String phone;
    boolean enable;
    String image;
    LocalDate dob;
    @Column(name="fullname")
    String fullName;
    @ManyToMany
    @JoinTable(
            name = "Users_roles",
            joinColumns = @JoinColumn(name = "User_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_name")
    )
    Set<Role> roles;

    @OneToOne(mappedBy = "user")
    Orders orders;


    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email + ", phone=" + phone + ", enable=" + enable + ", image=" + image + ", Dob" + dob  + ", Fullname: " + fullName + ", Roles: " + roles + "]";
    }
}
