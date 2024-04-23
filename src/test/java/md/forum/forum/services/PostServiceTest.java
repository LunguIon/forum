package md.forum.forum.services;

import md.forum.forum.models.Post;
import md.forum.forum.models.User;
import md.forum.forum.repositorys.PostRepository;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest implements WithAssertions {
    public static final String TITLE_POST = "The title of post";
    public static final String CONTENT_POST = "This is content";
    public static final int VALUE_OF_LIKE = 2;
    public static final String IMG_URL = "image url";
    public static final int ID = 1;
    public static final Long ID_LONG = 1L;
    public static final String EMAIl = "johndoe@gmail.com";
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
    @InjectMocks
    private PostService postService;

    @Test
    public void testCreatePost(){
        when(post.getTitle()).thenReturn(TITLE_POST);
        when(post.getContent()).thenReturn(CONTENT_POST);
        when(post.getValueOfLikes()).thenReturn(VALUE_OF_LIKE);
        when(postRepository.save(post)).thenReturn(post);

        assertThat(postService.createPost(post))
                .isNotNull()
                .returns(CONTENT_POST, Post::getContent)
                .returns(VALUE_OF_LIKE, Post::getValueOfLikes)
                .returns(TITLE_POST, Post::getTitle);

        verify(postRepository, times(1)).save(post);
    }

    @Test
    public void testGetAllPosts() {
        when(postRepository.findAll()).thenReturn(List.of(post, postSecond));

        assertThat(postService.getAllPosts()).hasSize(2)
                .contains(post, postSecond);

        verify(postRepository, times(1)).findAll();
    }

    @Test
    public void testGetPostById() {
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
    public void testGetPostById_IsEmpty() {
        when(postRepository.findById(ID_LONG)).thenReturn(Optional.empty());

        assertThat(postService.getPostById(ID_LONG))
                .isEmpty();

        verify(postRepository, times(1)).findById(ID_LONG);
    }

    @Test
    public void testGetPostById_IsNull(){
        assertThat(postService.getPostById(null)).isEmpty();
    }

    @Test
    public void testGetPostByUserName(){
        when(userService.getUserByEmail(EMAIl)).thenReturn(Optional.of(user));

        when(post.getId()).thenReturn(ID);
        when(post.getTitle()).thenReturn(TITLE_POST);
        when(post.getContent()).thenReturn(CONTENT_POST);

        when(postRepository.findAllByUser(user)).thenReturn(List.of(post));

        assertThat(postService.getPostsByUserName(EMAIl))
                .hasSize(1)
                .extracting(Post::getId, Post::getTitle, Post::getContent)
                .containsExactly(tuple(ID, TITLE_POST, CONTENT_POST));

        verify(userService, times(1)).getUserByEmail(EMAIl);
        verify(postRepository).findAllByUser(user);
    }

    @Test
    public void testGetPostByUserName_IsNull() {
        when(userService.getUserByEmail(null)).thenReturn(Optional.empty());

        assertThatThrownBy(()-> postService.getPostsByUserName(null))
                .isInstanceOf(NoSuchElementException.class);

        verify(userService, times(1)).getUserByEmail(null);
        verify(postRepository, never()).findAllByUser(any());
    }


    @Test
    public void testUpdatePost() {
        when(postRepository.findById(ID_LONG)).thenReturn(Optional.of(post));
        when(postSecond.getTitle()).thenReturn(TITLE_POST);
        when(postSecond.getContent()).thenReturn(CONTENT_POST);
        when(postSecond.getImageURL()).thenReturn(IMG_URL);
        when(postRepository.save(post)).thenReturn(post);

        assertThat(postService.updatePost(ID_LONG, postSecond))
                .isEqualTo(post);

        verify(postRepository).findById(ID_LONG);
        verify(postRepository).save(post);
    }

    @Test
    public void testUpdatePost_EmptyID() {
        when(postRepository.findById(ID_LONG)).thenReturn(Optional.empty());

        assertThat(postService.updatePost(ID_LONG, postSecond))
                .isNull();

        verify(postRepository).findById(ID_LONG);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    public void testUpdatePost_NullId(){
        assertThat(postService.updatePost(null, postSecond))
                .isNull();

        verify(postRepository).findById(null);
    }

    @Test
    public void testUpdatePost_NullNewComment() {
        when(postRepository.findById(ID_LONG)).thenReturn(Optional.of(post));

        assertThatThrownBy(()->postService.updatePost(ID_LONG, null))
                .isInstanceOf(NullPointerException.class);

        verify(postRepository).findById(ID_LONG);
        verifyNoMoreInteractions(postRepository);
    }

    @Test
    public void testDeletePost() {
        when(postRepository.existsById(ID_LONG)).thenReturn(true);
        doNothing().when(postRepository).deleteById(ID_LONG);

        assertThat(postService.deletePost(ID_LONG)).isTrue();

        verify(postRepository).deleteById(ID_LONG);
    }

    @Test
    public void testDeletePost_NotExist() {
        when(postRepository.existsById(ID_LONG)).thenReturn(false);

        assertThat(postService.deletePost(ID_LONG)).isFalse();

        verify(postRepository, never()).deleteById(ID_LONG);
    }








}
