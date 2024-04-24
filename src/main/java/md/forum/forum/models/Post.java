package md.forum.forum.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;


@Entity(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;
    @Column
    private String content;
    @Column(name = "image_url")
    private String imageURL;
    @Column(name = "value_of_likes")
    private int valueOfLikes;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;

    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_post_user"))
    @ManyToOne
    private User user;

}