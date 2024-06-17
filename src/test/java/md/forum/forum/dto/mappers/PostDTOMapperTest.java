package md.forum.forum.dto.mappers;

import md.forum.forum.dto.get.GetPostDTO;
import md.forum.forum.dto.get.GetUserDTO;
import md.forum.forum.models.Post;
import md.forum.forum.models.Topic;
import md.forum.forum.models.User;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
@ExtendWith(MockitoExtension.class)
class PostDTOMapperTest implements WithAssertions {
    @InjectMocks
    PostDTOMapper dtoMapper;
    @Mock
    User user;
    @Mock
    Date date;
    @Mock
    Topic topic;
    @Mock
    GetUserDTO userDTO;
    @Test
    void testShouldMapToPostDTO() {
        Post post = new Post(23, "23", "title", "content", "url", 42, date, date, user, topic);

        assertThat(dtoMapper.apply(post))
                .returns(post.getPostId(), GetPostDTO::postId)
                .returns(post.getTitle(), GetPostDTO::title)
                .returns(post.getContent(), GetPostDTO::content)
                .returns(post.getImageURL(), GetPostDTO::imageUrl)
                .returns(post.getCreateDate(), GetPostDTO::createdDate)
                .returns(post.getUpdateDate(), GetPostDTO::updateDate)
                .returns(userDTO, GetPostDTO::user);
    }

    @Test
    void testShouldMapToPostDTO_ToEmpty() {
        assertThatThrownBy(() -> dtoMapper.apply(new Post()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testShouldMapToPostDTO_ToNull() {
        assertThatThrownBy(() -> dtoMapper.apply(null))
                .isInstanceOf(NullPointerException.class);
    }
}