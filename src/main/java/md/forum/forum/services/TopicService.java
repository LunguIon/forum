package md.forum.forum.services;

import lombok.RequiredArgsConstructor;
import md.forum.forum.dto.SimplifiedTopicDTO;
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

    public Optional<Topic> findTopicById(int id) {
        return topicRepository.findById(id);
    }

    public Optional<Topic> findTopicByTitle(String title) {
        return topicRepository.findByTitle(title);
    }

    public Optional<List<Topic>> findTopicsByUserEmail(String email) {
        return topicRepository.findByUserEmail(email);
    }

    public Optional<List<Topic>> findAllTopicsOrderByTitleDesc() {
        return topicRepository.findAllByOrderByTitleDesc();
    }

    public Topic createTopic(SimplifiedTopicDTO simplifiedTopicDTO) {
        Topic topic = new Topic();
        topic.setTitle(simplifiedTopicDTO.getTitle());
        topic.setContent(simplifiedTopicDTO.getContent());
        topic.setImageURL(simplifiedTopicDTO.getImageURL());
        User user = userService.getUserByEmail(simplifiedTopicDTO.getEmail()).orElseThrow();
        topic.setUser(user);
        return topicRepository.save(topic);
    }

    public void deleteTopicByTitle(String title) {
        topicRepository.deleteByTitle(title);
    }

}
