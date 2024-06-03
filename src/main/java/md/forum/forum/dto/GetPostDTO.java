package md.forum.forum.dto;

import java.sql.Date;

public record GetPostDTO(
        String postId,
        UserDTO user,
        String title,
        String content,
        String imageUrl,
        Date createdDate
) {
}
