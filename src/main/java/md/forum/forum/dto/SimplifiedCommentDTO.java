package md.forum.forum.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimplifiedCommentDTO {
    private String email;
    private String postId;
    private String content;

}
