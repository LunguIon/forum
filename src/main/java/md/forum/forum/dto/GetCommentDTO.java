package md.forum.forum.dto;

public record GetCommentDTO(
        String commentId,
        UserDTO userDTO,
        String content,
        String postId,
        String valueOfLikes
) {


}
