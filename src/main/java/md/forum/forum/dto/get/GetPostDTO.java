package md.forum.forum.dto.get;

import java.sql.Date;

public record GetPostDTO(
        String postId,
        UserDTO user,
        String title,
        String content,
        String imageUrl,
        Date createdDate,
        Date updateDate
) {
}
