package md.forum.forum.services;

import md.forum.forum.models.Comment;
import md.forum.forum.models.Like;
import md.forum.forum.models.Post;
import md.forum.forum.models.User;
import md.forum.forum.repositorys.CommentRepository;
import md.forum.forum.repositorys.LikeRepository;
import md.forum.forum.repositorys.PostRepository;
import md.forum.forum.repositorys.UserRepository;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest implements WithAssertions {
    public static final String EMAIL = "jonhdoe@gmail.com";
    public static final Long POST_ID = 2L;
    public static final Long COMM_ID = 1L;
    @Mock
    private Like like;
    @Mock
    private Like likeSecond;
    @Mock
    private User user;
    @Mock
    private Post post;
    @Mock
    private Comment comment;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private LikeRepository likeRepository;
    @InjectMocks
    private LikeService likeService;

    @Test
    public void testFindAll() {
        when(likeRepository.findAll()).thenReturn(List.of(like, likeSecond));

        assertThat(likeService.findAll())
                .hasSize(2)
                .contains(like, likeSecond);

        verify(likeRepository, times(1)).findAll();
    }

    @Test
    public void testFindAll_IsEmpty() {
        when(likeRepository.findAll()).thenReturn(List.of());
        assertThat(likeService.findAll())
                .isEmpty();
        verify(likeRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllByUser() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(likeRepository.findAllByUser(user)).thenReturn(List.of(like));

        assertThat(likeService.findAllByUser(EMAIL))
                .contains(like);

        verify(userRepository, times(1)).findByEmail(EMAIL);
        verify(likeRepository, times(1)).findAllByUser(user);
    }

    @Test
    public void testFindAllByUser_IsEmpty() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(likeRepository.findAllByUser(user)).thenReturn(List.of());

        assertThat(likeService.findAllByUser(EMAIL))
                .isEmpty();

        verify(userRepository, times(1)).findByEmail(EMAIL);
        verify(likeRepository, times(1)).findAllByUser(user);
    }

    @Test
    public void testFindAllByPost() {
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
        when(likeRepository.findAllByPost(post)).thenReturn(List.of(like));

        assertThat(likeService.findAllByPost(Math.toIntExact(POST_ID)))
                .contains(like);

        verify(postRepository, times(1)).findById(POST_ID);
        verify(likeRepository, times(1)).findAllByPost(post);
    }

    @Test
    public void testFindAllByPost_IsEmpty() {
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
        when(likeRepository.findAllByPost(post)).thenReturn(List.of());

        assertThat(likeService.findAllByPost(Math.toIntExact(POST_ID)))
                .isEmpty();

        verify(postRepository, times(1)).findById(POST_ID);
        verify(likeRepository, times(1)).findAllByPost(post);
    }

    @Test
    public void testFindAllForPost_ZeroParam() {
        when(likeRepository.findAllForPost()).thenReturn(List.of(like));

        assertThat(likeService.findAllForPost()).contains(like);

        verify(likeRepository, times(1)).findAllForPost();
    }

    @Test
    public void testFindAllForPost_ZeroParam_IsEmpty() {
        when(likeRepository.findAllForPost()).thenReturn(List.of());

        assertThat(likeService.findAllForPost()).isEmpty();

        verify(likeRepository, times(1)).findAllForPost();
    }

    @Test
    public void testFindAllForPost_OneParam() {
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
        when(likeRepository.findAllForPost(post)).thenReturn(List.of(like));

        assertThat(likeService.findAllForPost(Math.toIntExact(POST_ID)))
                .contains(like);

        verify(postRepository, times(1)).findById(POST_ID);
        verify(likeRepository, times(1)).findAllForPost(post);
    }

    @Test
    public void testFindAllForPost_OneParam_IsEmpty() {
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
        when(likeRepository.findAllForPost(post)).thenReturn(List.of());

        assertThat(likeService.findAllForPost(Math.toIntExact(POST_ID)))
                .isEmpty();

        verify(postRepository, times(1)).findById(POST_ID);
        verify(likeRepository, times(1)).findAllForPost(post);
    }

    @Test
    public void testFindAllForComments_ZeroParam(){
        when(likeRepository.findAllForComments()).thenReturn(List.of(like));

        assertThat(likeService.findAllForComments()).contains(like);

        verify(likeRepository, times(1)).findAllForComments();
    }

    @Test
    public void testFindAllForComments_ZeroParam_IsEmpty(){
        when(likeRepository.findAllForComments()).thenReturn(List.of());

        assertThat(likeService.findAllForComments())
                .isEmpty();

        verify(likeRepository, times(1)).findAllForComments();
    }

    @Test
    public void testFindAllForComments_OneParam(){
        when(commentRepository.findById(COMM_ID)).thenReturn(Optional.of(comment));
        when(likeRepository.findAllForCommentsBy(comment)).thenReturn(List.of(like));

        assertThat(likeService.findAllForComments(Math.toIntExact(COMM_ID)))
                .contains(like);

        verify(commentRepository, times(1)).findById(COMM_ID);
        verify(likeRepository, times(1)).findAllForCommentsBy(comment);
    }

    @Test
    public void testFindAllForComments_OneParam_IsEmpty(){
        when(commentRepository.findById(COMM_ID)).thenReturn(Optional.of(comment));
        when(likeRepository.findAllForCommentsBy(comment)).thenReturn(List.of());

        assertThat(likeService.findAllForComments(Math.toIntExact(COMM_ID)))
                .isEmpty();

        verify(commentRepository, times(1)).findById(COMM_ID);
        verify(likeRepository, times(1)).findAllForCommentsBy(comment);
    }
}