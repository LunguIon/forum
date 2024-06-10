package md.forum.forum.services;

import md.forum.forum.dto.get.GetPostDTO;
import md.forum.forum.dto.mappers.PostDTOMapper;
import md.forum.forum.dto.simplified.SimplifiedPostDTO;
import md.forum.forum.models.Post;
import md.forum.forum.models.Topic;
import md.forum.forum.models.User;
import md.forum.forum.repository.PostRepository;
import md.forum.forum.s3.S3Buckets;
import md.forum.forum.s3.S3Service;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.sql.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest implements WithAssertions {
    static final String TITLE_POST = "The title of post";
    static final String CONTENT_POST = "This is content";
    static final String IMG_URL = "image url";
    static final int ID = 1;
    static final Long ID_LONG = 1L;
    public static final String ID_STRING = "1";
    static final String EMAIl = "johndoe@gmail.com";
    @Mock
    private PostRepository postRepository;
    @Mock
    private Post post;
    @Mock
    private Post postSecond;
    @Mock
    private User user;
    @Mock
    private UserService userService;
    @Mock
    private PostDTOMapper postDTOMapper;
    @Mock
    private GetPostDTO getPostDTO;
    @Mock
    Topic topic;
    @Mock
    SimplifiedPostDTO simplifiedPostDTO;
    @Mock
    TopicService topicService;
    @Mock
    Date date;
    @Mock
    MultipartFile file;
    @Mock
    S3Service s3Service;
    @Mock
    S3Buckets s3Buckets;
    @InjectMocks
    private PostService postService;

    @Test
    void testCreatePost() {
        when(post.getPostId()).thenReturn(ID_STRING);
        when(post.getUser()).thenReturn(user);
        when(postRepository.save(post)).thenReturn(post);

        assertThat(postService.createPost(post))
                .isNotNull()
                .isEqualTo(post)
                .returns(ID_STRING, Post::getPostId)
                .returns(user, Post::getUser);

        verify(postRepository).save(post);
    }

    @Test
    void testCreatePost_IsNull() {
        assertThat(postService.createPost(post)).isNull();
    }

    @Test
    void testCreatePostDTO() {
        when(simplifiedPostDTO.getTitle()).thenReturn(TITLE_POST);
        when(simplifiedPostDTO.getContent()).thenReturn(CONTENT_POST);
        when(simplifiedPostDTO.getImageURL()).thenReturn(IMG_URL);
        when(topicService.findTopicByTitleFull(simplifiedPostDTO.getTopicTitle())).thenReturn(Optional.of(topic));
        when(userService.getUserByEmailFull(simplifiedPostDTO.getEmail())).thenReturn(Optional.of(user));

        when(post.getPostId()).thenReturn(ID_STRING);
        when(post.getTitle()).thenReturn(TITLE_POST);
        when(post.getContent()).thenReturn(CONTENT_POST);
        when(post.getImageURL()).thenReturn(IMG_URL);
        when(post.getUser()).thenReturn(user);
        when(post.getTopic()).thenReturn(topic);

        when(postRepository.save(any(Post.class))).thenReturn(post);

        assertThat(postService.createPost(simplifiedPostDTO))
                .isNotNull()
                .isEqualTo(post)
                .returns(ID_STRING, Post::getPostId)
                .returns(TITLE_POST, Post::getTitle)
                .returns(CONTENT_POST,Post::getContent)
                .returns(IMG_URL, Post::getImageURL)
                .returns(user, Post::getUser)
                .returns(topic, Post::getTopic);

        verify(topicService).findTopicByTitleFull(simplifiedPostDTO.getTopicTitle());
        verify(userService).getUserByEmailFull(simplifiedPostDTO.getEmail());
        verify(postRepository).save(any(Post.class));

    }

    @Test
    public void testGetAllPosts() {
        when(postRepository.findAll()).thenReturn(List.of(post));
        when(postDTOMapper.apply(post)).thenReturn(getPostDTO);

        assertThat(postService.getAllPosts())
                .isEqualTo(List.of(getPostDTO));

        verify(postRepository).findAll();
        verify(postDTOMapper).apply(post);
    }

    @Test
    public void testGetAllPosts_IsEmpty() {
        assertThat(postService.getAllPosts()).isEmpty();
    }

    @Test
    void testGetPostById() {
        when(post.getId()).thenReturn(ID);
        when(postRepository.findById((long)ID)).thenReturn(Optional.of(post));

        assertThat(postService.getPostById((long) post.getId()))
                .isPresent()
                .get()
                .extracting(Post::getId)
                .isEqualTo(post.getId());

        verify(postRepository, times(1)).findById((long) post.getId());
    }

    @Test
    void testGetPostById_IsEmpty() {
        when(postRepository.findById(ID_LONG)).thenReturn(Optional.empty());

        assertThat(postService.getPostById(ID_LONG)).isEmpty();

        verify(postRepository, times(1)).findById(ID_LONG);
    }

    @Test
    void testGetPostById_IsNull() {
        assertThat(postService.getPostById(null)).isEmpty();
    }

    @Test
    void testGetPostByPostId() {
        when(postDTOMapper.apply(post)).thenReturn(getPostDTO);
        when(postRepository.findPostByPostId(ID_STRING)).thenReturn(Optional.of(post));

        assertThat(postService.getPostByPostId(ID_STRING))
                .contains(getPostDTO);

        verify(postRepository).findPostByPostId(ID_STRING);
        verify(postDTOMapper).apply(post);
    }

    @Test
    void testGetPostByPostId_IsNUll() {
        assertThat(postService.getPostByPostId(ID_STRING)).isEmpty();
    }

    @Test
    void testGetPostByPostIdFull() {
        when(postRepository.findPostByPostId(ID_STRING)).thenReturn(Optional.of(post));

        assertThat(postService.getPostByPostIdFull(ID_STRING))
                .isNotNull()
                .isEqualTo(Optional.of(post));

        verify(postRepository).findPostByPostId(ID_STRING);
    }

    @Test
    void testGetPostByPostIdFull_IsNull () {
        assertThat(postService.getPostByPostIdFull(ID_STRING)).isEmpty();
    }

    @Test
    void testGetPostsByUserName() {
        when(userService.getUserByEmailFull(EMAIl)).thenReturn(Optional.of(user));
        when(postRepository.findAllByUser(user)).thenReturn(List.of(post));
        when(postDTOMapper.apply(post)).thenReturn(getPostDTO);

        assertThat(postService.getPostsByUserName(EMAIl))
                .isEqualTo(List.of(getPostDTO));

        verify(userService).getUserByEmailFull(EMAIl);
        verify(postRepository).findAllByUser(user);
        verify(postDTOMapper).apply(post);
    }

    @Test
    void testGetPostsByUserName_Null() {
        assertThatThrownBy(() -> postService.getPostsByUserName(EMAIl))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testGetPostsByTopic() {
        when(topicService.findTopicByTitleFull(EMAIl)).thenReturn(Optional.of(topic));
        when(postRepository.findAllByTopic(topic)).thenReturn(List.of(post));
        when(postDTOMapper.apply(post)).thenReturn(getPostDTO);

        assertThat(postService.getPostsByTopic(EMAIl))
                .isEqualTo(List.of(getPostDTO));

        verify(topicService).findTopicByTitleFull(EMAIl);
        verify(postRepository).findAllByTopic(topic);
        verify(postDTOMapper).apply(post);
    }

    @Test
    void testGetPostsByTopic_IsNull() {
        assertThatThrownBy(() -> postService.getPostsByTopic(EMAIl))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testUpdatePost() {
        when(postRepository.findById(ID_LONG)).thenReturn(Optional.of(post));
        when(postSecond.getTitle()).thenReturn(TITLE_POST);
        when(postSecond.getContent()).thenReturn(CONTENT_POST);
        when(postSecond.getImageURL()).thenReturn(IMG_URL);
        when(postRepository.save(post)).thenReturn(post);

        assertThat(postService.updatePost(ID_LONG, postSecond)).isEqualTo(post);

        verify(postRepository).findById(ID_LONG);
        verify(postRepository).save(post);
    }

    @Test
    void testUpdatePost_EmptyID() {
        when(postRepository.findById(ID_LONG)).thenReturn(Optional.empty());

        assertThat(postService.updatePost(ID_LONG, postSecond)).isNull();

        verify(postRepository).findById(ID_LONG);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void testUpdatePost_NullId() {
        assertThat(postService.updatePost(null, postSecond)).isNull();
    }


    @Test
    void testUpdatePostByPostId() {
        when(topicService.findTopicByTitleFull(simplifiedPostDTO.getTopicTitle())).thenReturn(Optional.of(topic));

        when(post.getTitle()).thenReturn(TITLE_POST);
        when(post.getContent()).thenReturn(CONTENT_POST);
        when(post.getUpdateDate()).thenReturn(date);
        when(post.getImageURL()).thenReturn(IMG_URL);
        when(post.getTopic()).thenReturn(topic);

        when(postRepository.findPostByPostId(ID_STRING)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);

        assertThat(postService.updatePostByPostId(ID_STRING, simplifiedPostDTO))
                .isNotNull()
                .isEqualTo(post)
                .returns(TITLE_POST, Post::getTitle)
                .returns(CONTENT_POST, Post::getContent)
                .returns(date, Post::getUpdateDate)
                .returns(IMG_URL, Post::getImageURL)
                .returns(topic, Post::getTopic);

        verify(topicService).findTopicByTitleFull(simplifiedPostDTO.getTopicTitle());
        verify(postRepository).findPostByPostId(ID_STRING);
        verify(postRepository).save(post);
    }

    @Test
    void testUpdatePostByPostId_IsNull() {
        assertThat(postService.updatePostByPostId(ID_STRING, simplifiedPostDTO))
                .isNull();
    }

    @Test
    void testDeletePost() {
        when(postRepository.existsById(ID_LONG)).thenReturn(true);
        doNothing().when(postRepository).deleteById(ID_LONG);

        assertThat(postService.deletePost(ID_LONG)).isTrue();

        verify(postRepository).deleteById(ID_LONG);
    }

    @Test
    void testDeletePost_NotExist() {
        when(postRepository.existsById(ID_LONG)).thenReturn(false);

        assertThat(postService.deletePost(ID_LONG)).isFalse();

        verify(postRepository, never()).deleteById(ID_LONG);
    }

    @Test
    void testDeletePostById() {
        when(postRepository.existsByPostId(ID_STRING)).thenReturn(true);
        doNothing().when(postRepository).deleteByPostId(ID_STRING);

        assertThat(postService.deletePostById(ID_STRING)).isTrue();

        verify(postRepository).deleteByPostId(ID_STRING);
    }

    @Test
    void testDeletePostById_NonExist() {
        when(postRepository.existsByPostId(ID_STRING)).thenReturn(false);

        assertThat(postService.deletePostById(ID_STRING)).isFalse();

        verify(postRepository, never()).deleteByPostId(ID_STRING);
    }

    @Test
    void testUpdatePost_NullNewComment() {
        when(postRepository.findById(ID_LONG)).thenReturn(Optional.of(post));

        assertThatThrownBy(() -> postService.updatePost(ID_LONG, null))
                .isInstanceOf(NullPointerException.class);

        verify(postRepository).findById(ID_LONG);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    void testUploadPostImage_PostNotFound() {
        when(postRepository.existsByPostId(ID_STRING)).thenReturn(false);

        assertThatThrownBy(() -> postService.uploadPostImage(ID_STRING, file))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(ChangeSetPersister.NotFoundException.class);
    }

    @Test
    void testUploadPostImage_IOException() throws IOException {
        String ID_STRING = "validPostId";
        when(postRepository.existsByPostId(ID_STRING)).thenReturn(true);
        when(file.getBytes()).thenThrow(new IOException("Simulated IOException"));

        assertThatThrownBy(() -> postService.uploadPostImage(ID_STRING, file))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(IOException.class);
    }

    @Test
    void testUploadPostImage_Success() throws IOException {
        UUID fixedUUID = UUID.fromString("cebb4ccf-d203-40c0-938a-a5c79a40a7d7");
        try (MockedStatic<UUID> mockedUUID = Mockito.mockStatic(UUID.class)) {
            mockedUUID.when(UUID::randomUUID).thenReturn(fixedUUID);

            when(postRepository.existsByPostId(ID_STRING)).thenReturn(true);
            when(file.getBytes()).thenReturn(new byte[] {1, 2, 3, 4});
            when(s3Buckets.getPosts()).thenReturn(TITLE_POST);

            postService.uploadPostImage(ID_STRING, file);

            verify(s3Service).putObject(
                    eq(TITLE_POST),
                    eq("postImage/" + ID_STRING + "/" + fixedUUID),
                    any(byte[].class)
            );
            verify(postRepository).updateImageByPostId(ID_STRING, fixedUUID.toString());
        }
    }

    @Test
    void testGetPostImage_PostNotFound() {
        when(postRepository.findPostByPostId(ID_STRING)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.getPostImage(ID_STRING))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void testGetPostImage_PostIdContainsHttps() {
        when(post.getPostId()).thenReturn(IMG_URL);
        when(postRepository.findPostByPostId(ID_STRING)).thenReturn(Optional.of(post));

        assertThat( postService.getPostImage(ID_STRING)).isNull();
    }

    @Test
    void testGetPostImage_ValidPost() {
        Post post = new Post();
        post.setPostId("validId");
        byte[] expectedBytes = {1, 2, 3, 4};
        when(postRepository.findPostByPostId("validId")).thenReturn(Optional.of(post));
        when(s3Buckets.getPosts()).thenReturn("myBucket");
        when(s3Service.getObject("myBucket", "postImage/validId/validId")).thenReturn(expectedBytes);

        byte[] result = postService.getPostImage("validId");

        assertThat(result).isEqualTo(expectedBytes);
        verify(s3Service).getObject("myBucket", "postImage/validId/validId");
    }
}
