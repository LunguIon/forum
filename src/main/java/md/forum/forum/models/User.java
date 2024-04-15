package md.forum.forum.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column
    private String username;
    @Column
    private String email;
    @Column(name = "password_hash")
    private String passwordHash;
    @Column(name = "image_url_profile")
    private String imageUrlProfile;
    @Column(name = "create_date")
    private Date createDate;
    @JoinColumn(name = "id_role",foreignKey = @ForeignKey(name="fk_user_role"))
    @ManyToOne()
    private Role role;
    @Column
    private boolean banned;
}
