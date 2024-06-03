package md.forum.forum.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimplifiedLikeDTO {
    boolean upvote;
    String postId;
    String commentId;
    String userEmail;
}
