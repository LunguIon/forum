package md.forum.forum.dto.get;

import java.sql.Date;

public record GetCommentDTO(
        String commentId,
        GetUserDTO user,
        String content,
        String postId,
        long valueOfLikes,
        Date creationDate,
        Date updateDate
) {


}
