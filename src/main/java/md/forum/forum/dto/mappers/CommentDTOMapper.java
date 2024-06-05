package md.forum.forum.dto.mappers;

import md.forum.forum.dto.get.GetCommentDTO;
import md.forum.forum.models.Comment;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class CommentDTOMapper implements Function<Comment, GetCommentDTO> {

    @Override
    public GetCommentDTO apply(Comment comment) {
        return new GetCommentDTO(
                comment.getCommentId(),
                new UserDTOMapper().apply(comment.getUser()),
                comment.getContent(),
                comment.getPost().getPostId(),
                comment.getNumberOfLikes(),
                comment.getCreationDate(),
                comment.getUpdateDate()
        );
    }
}
