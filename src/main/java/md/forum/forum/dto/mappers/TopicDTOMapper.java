package md.forum.forum.dto.mappers;

import md.forum.forum.dto.get.GetTopicDTO;
import md.forum.forum.models.Topic;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class TopicDTOMapper implements Function<Topic, GetTopicDTO> {
    @Override
    public GetTopicDTO apply(Topic topic) {
        return new GetTopicDTO(
                topic.getTitle(),
                topic.getContent(),
                topic.getImageURL(),
                new UserDTOMapper().apply(topic.getUser())
        );
    }
}
