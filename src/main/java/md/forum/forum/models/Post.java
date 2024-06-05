package md.forum.forum.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.UUID;

@Entity(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true, nullable = false)
    private String postId;
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
    @JoinColumn(name = "topic_id", foreignKey = @ForeignKey(name = "fk_post_topic"))
    @ManyToOne
    private Topic topic;

    public void setPostId() {
        this.postId = UUID.randomUUID().toString();
    }
}