package md.forum.forum.dto.mappers;

import md.forum.forum.dto.get.TopicDTO;
import md.forum.forum.models.Topic;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class TopicDTOMapper implements Function<Topic, TopicDTO> {
    @Override
    public TopicDTO apply(Topic topic) {
        return new TopicDTO(
                topic.getTitle(),
                topic.getContent(),
                topic.getImageURL(),
                new UserDTOMapper().apply(topic.getUser())
        );
    }
}
