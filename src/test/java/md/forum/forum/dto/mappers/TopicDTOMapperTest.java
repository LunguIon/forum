package md.forum.forum.dto.mappers;

import md.forum.forum.dto.get.TopicDTO;
import md.forum.forum.dto.get.UserDTO;
import md.forum.forum.models.Topic;
import md.forum.forum.models.User;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TopicDTOMapperTest implements WithAssertions {
    @InjectMocks
    TopicDTOMapper dtoMapper;
    @Mock
    User user;
    @Mock
    UserDTO userDTO;

    @Test
    void testShouldMapToTopicDTO() {
        Topic topic = new Topic(1, "Title", "Content", "IMg", user);

        assertThat(dtoMapper.apply(topic))
                .returns(topic.getTitle(), TopicDTO::title)
                .returns(topic.getContent(), TopicDTO::content)
                .returns(topic.getImageURL(), TopicDTO::imageUrl)
                .returns(userDTO, TopicDTO::user);
    }

    @Test
    void testShouldMapToTopicDTO_ToEmpty() {
        assertThatThrownBy(() -> dtoMapper.apply(new Topic()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testShouldMapToTopicDTO_ToNull() {
        assertThatThrownBy(() -> dtoMapper.apply(null))
                .isInstanceOf(NullPointerException.class);
    }
}
