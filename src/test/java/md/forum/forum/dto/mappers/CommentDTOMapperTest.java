package md.forum.forum.dto.mappers;

import md.forum.forum.dto.get.GetCommentDTO;
import md.forum.forum.dto.get.GetUserDTO;
import md.forum.forum.models.Comment;
import md.forum.forum.models.Post;
import md.forum.forum.models.User;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;

@ExtendWith(MockitoExtension.class)
public class CommentDTOMapperTest implements WithAssertions {
    @InjectMocks
    CommentDTOMapper dtoMapper;
    @Mock
    User user;
    @Mock
    Date date;
    @Mock
    Post post;
    @Mock
    GetUserDTO userDTO;

    @Test
    void testShouldMapToCommentDTO() {
        Comment comment = new Comment(23, "23", "content", 23, date, date, post, user);

        assertThat(dtoMapper.apply(comment))
                .returns(comment.getCommentId(), GetCommentDTO::commentId)
                .returns(comment.getContent(), GetCommentDTO::content)
                .returns(comment.getPost().getPostId(), GetCommentDTO::postId)
                .returns( (long) comment.getNumberOfLikes(), GetCommentDTO::valueOfLikes)
                .returns(comment.getCreationDate(), GetCommentDTO::creationDate)
                .returns(comment.getUpdateDate(), GetCommentDTO::updateDate)
                .returns(userDTO, GetCommentDTO::user);
    }

    @Test
    void testShouldMapToCommentDTO_ToEmpty() {
        assertThatThrownBy(() -> dtoMapper.apply(new Comment()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testShouldMapToCommentDTO_ToNull() {
        assertThatThrownBy(() -> dtoMapper.apply(null))
                .isInstanceOf(NullPointerException.class);
    }

}
