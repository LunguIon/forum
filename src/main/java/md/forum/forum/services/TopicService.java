package md.forum.forum.services;

import lombok.RequiredArgsConstructor;
import md.forum.forum.dto.get.TopicDTO;
import md.forum.forum.dto.mappers.TopicDTOMapper;
import md.forum.forum.dto.simplified.SimplifiedTopicDTO;
import md.forum.forum.models.Topic;
import md.forum.forum.models.User;
import md.forum.forum.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;
    private final UserService userService;
    private final TopicDTOMapper topicDTOMapper;

    public Optional<Topic> findTopicById(int id) {
        return topicRepository.findById(id);
    }

    public Optional<TopicDTO> findTopicByTitle(String title) {
        return topicRepository.findByTitle(title)
                .map(topicDTOMapper);
    }
    public Optional<Topic> findTopicByTitleFull(String title) {
        return topicRepository.findByTitle(title);
    }

    public List<TopicDTO> findTopicsByUserEmail(String email) {
        return topicRepository.findByUserEmail(email)
                .stream()
                .map(topicDTOMapper)
                .toList();
    }

    public List<TopicDTO> findAllTopicsOrderByTitleDesc() {
        return topicRepository.findAllByOrderByTitleDesc()
                .stream()
                .map(topicDTOMapper)
                .toList();
    }

    public Topic createTopic(SimplifiedTopicDTO simplifiedTopicDTO) {
        Topic topic = new Topic();
        topic.setTitle(simplifiedTopicDTO.getTitle());
        topic.setContent(simplifiedTopicDTO.getContent());
        topic.setImageURL(simplifiedTopicDTO.getImageURL());
        User user = userService.getUserByEmailFull(simplifiedTopicDTO.getEmail()).orElseThrow();
        topic.setUser(user);
        return topicRepository.save(topic);
    }

    public void deleteTopicByTitle(String title) {
        topicRepository.deleteByTitle(title);
    }

}
