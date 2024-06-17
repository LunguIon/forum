package md.forum.forum.services;

import md.forum.forum.dto.get.GetTopicDTO;
import md.forum.forum.dto.mappers.TopicDTOMapper;
import md.forum.forum.dto.simplified.SimplifiedTopicDTO;
import md.forum.forum.models.Topic;
import md.forum.forum.models.User;
import md.forum.forum.repository.TopicRepository;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TopicServiceTest implements WithAssertions {
    public static final int ID = 12;
    public static final String TITLE = "title";
    public static final String EMAIL = "email";
    public static final String TEXT = "text";
    @Mock
    Topic topic;
    @Mock
    User user;
    @Mock
    UserService userService;
    @Mock
    TopicRepository topicRepository;
    @Mock
    TopicDTOMapper topicDTOMapper;
    @Mock
    GetTopicDTO topicDTO;
    @Mock
    SimplifiedTopicDTO simplifiedTopicDTO;
    @InjectMocks
    TopicService topicService;

    @Test
    void testFindTopicById() {
        when(topicRepository.findById(ID)).thenReturn(Optional.of(topic));

        assertThat(topicService.findTopicById(ID))
                .contains(topic);

        verify(topicRepository).findById(ID);
    }

    @Test
    void testFindTopicById_IsNull() {
        assertThat(topicService.findTopicById(ID)).isEmpty();
    }

    @Test
    void testFindTopicByTitle() {
        when(topicDTOMapper.apply(topic)).thenReturn(topicDTO);
        when(topicRepository.findByTitle(TITLE)).thenReturn(Optional.of(topic));
        when(topicDTO.title()).thenReturn(TITLE);

        assertThat(topicService.findTopicByTitle(TITLE))
                .isEqualTo(Optional.of(topicDTO))
                .map(GetTopicDTO::title)
                .contains(TITLE);

        verify(topicDTOMapper).apply(topic);
        verify(topicRepository).findByTitle(TITLE);
    }

    @Test
    void testFindTopicByTitle_IsNull() {
        assertThat(topicService.findTopicByTitle(TITLE)).isEmpty();
    }

    @Test
    void testFindTopicByTitleFull() {
        when(topic.getTitle()).thenReturn(TITLE);
        when(topicRepository.findByTitle(TITLE)).thenReturn(Optional.of(topic));

        assertThat(topicService.findTopicByTitleFull(TITLE))
                .isEqualTo(Optional.of(topic))
                .map(Topic::getTitle)
                .contains(TITLE);

        verify(topicRepository).findByTitle(TITLE);
    }

    @Test
    void testFindTopicByTitleFull_IsEmpty() {
        assertThat(topicService.findTopicByTitleFull(TITLE)).isEmpty();
    }

    @Test
    void testFindTopicsByUserEmail() {
        when(topicRepository.findByUserEmail(EMAIL)).thenReturn(List.of(topic));
        when(topicDTOMapper.apply(topic)).thenReturn(topicDTO);

        assertThat(topicService.findTopicsByUserEmail(EMAIL))
                .isEqualTo(List.of(topicDTO));

        verify(topicRepository).findByUserEmail(EMAIL);
        verify(topicDTOMapper).apply(topic);
    }

    @Test
    void testFindTopicsByUserEmail_IsNull() {
        assertThat(topicService.findTopicsByUserEmail(EMAIL)).isEmpty();
    }

    @Test
    void testFindAllTopicsOrderByTitleDesc() {
        when(topicRepository.findAllByOrderByTitleDesc()).thenReturn(List.of(topic));
        when(topicDTOMapper.apply(topic)).thenReturn(topicDTO);

        assertThat(topicService.findAllTopicsOrderByTitleDesc())
                .contains(topicDTO);

        verify(topicDTOMapper).apply(topic);
        verify(topicRepository).findAllByOrderByTitleDesc();
    }

    @Test
    void testFindAllTopicsOrderByTitleDesc_IsEmpty() {
        assertThat(topicService.findAllTopicsOrderByTitleDesc()).isEmpty();
    }

    @Test
    void testCreateTopic() {
        when(simplifiedTopicDTO.getContent()).thenReturn(TEXT);
        when(simplifiedTopicDTO.getTitle()).thenReturn(TITLE);
        when(simplifiedTopicDTO.getImageURL()).thenReturn(TEXT);
        when(userService.getUserByEmailFull(simplifiedTopicDTO.getEmail())).thenReturn(Optional.of(user));

        when(topic.getTitle()).thenReturn(TITLE);
        when(topic.getContent()).thenReturn(TEXT);
        when(topic.getImageURL()).thenReturn(TEXT);
        when(topic.getUser()).thenReturn(user);

        when(topicRepository.save(any(Topic.class))).thenReturn(topic);

        assertThat(topicService.createTopic(simplifiedTopicDTO))
                .isNotNull()
                .isEqualTo(topic)
                .returns(TEXT, Topic::getContent)
                .returns(TITLE, Topic::getTitle)
                .returns(TEXT, Topic::getImageURL)
                .returns(user, Topic::getUser);

        verify(userService).getUserByEmailFull(simplifiedTopicDTO.getEmail());
        verify(topicRepository).save(any(Topic.class));
    }

    @Test
    void testDeleteTopicByTitle() {
        doNothing().when(topicRepository).deleteByTitle(TITLE);
        topicService.deleteTopicByTitle(TITLE);
        verify(topicRepository).deleteByTitle(TITLE);
    }
}
