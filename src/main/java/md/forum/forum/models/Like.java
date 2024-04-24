package md.forum.forum.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Entity(name = "likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private boolean upvote;
    @Column(name = "create_date")
    private Date createDate;
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "fk_like_post"))
    @ManyToOne
    private Post post;
    @JoinColumn(name = "comment_id", foreignKey = @ForeignKey(name = "fk_like_comment"))
    @ManyToOne
    private Comment comments;
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_like_user"))
    @ManyToOne
    private User user;

}
