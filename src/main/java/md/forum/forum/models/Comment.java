package md.forum.forum.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String content;
    @Column(name = "value_of_like")
    private int numberOfLikes;
    @Column(name = "create_date")
    private Date creationDate;
    @Column(name = "update_date")
    private Date updateDate;
    @JoinColumn(name = "post_id",foreignKey = @ForeignKey(name = "fk_comment_post"))
    @ManyToOne
    private Post post;
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(name = "fk_comment_user"))
    @ManyToOne
    private User user;

}