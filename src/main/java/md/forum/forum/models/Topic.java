package md.forum.forum.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "topics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true,nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(name = "image_url")
    private String imageURL;
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_topic_user"))
    @ManyToOne(cascade=CascadeType.DETACH)
    private User user;
}
