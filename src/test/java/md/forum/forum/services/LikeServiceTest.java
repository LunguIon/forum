package md.forum.forum.services;

import md.forum.forum.dto.simplified.SimplifiedLikeDTO;
import md.forum.forum.models.Comment;
import md.forum.forum.models.Like;
import md.forum.forum.models.Post;
import md.forum.forum.models.User;
import md.forum.forum.repository.CommentRepository;
import md.forum.forum.repository.LikeRepository;
import md.forum.forum.repository.PostRepository;
import md.forum.forum.repository.UserRepository;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest implements WithAssertions {
    static final String EMAIL = "jonhdoe@gmail.com";
    static final Long POST_ID = 2L;
    static final Long COMM_ID = 1L;
    static final String LIKE_ID = "12";
    public static final int COUNT = 12;
    @Mock
    Like like;
    @Mock
    Like likeSecond;
    @Mock
    User user;
    @Mock
    Post post;
    @Mock
    Comment comment;
    @Mock
    Date date;
    @Mock
    CommentRepository commentRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    PostRepository postRepository;
    @Mock
    LikeRepository likeRepository;
    @Mock
    SimplifiedLikeDTO simplifiedLikeDTO;
    @InjectMocks
    LikeService likeService;

    @Test
    void testFindAll() {
        when(likeRepository.findAll()).thenReturn(List.of(like, likeSecond));

        assertThat(likeService.findAll()).hasSize(2).contains(like, likeSecond);

        verify(likeRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_IsEmpty() {
        when(likeRepository.findAll()).thenReturn(List.of());
        assertThat(likeService.findAll()).isEmpty();
        verify(likeRepository, times(1)).findAll();
    }

    @Test
    void testFindAllByUser() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(likeRepository.findAllByUser(user)).thenReturn(List.of(like));

        assertThat(likeService.findAllByUser(EMAIL)).contains(like);

        verify(userRepository, times(1)).findByEmail(EMAIL);
        verify(likeRepository, times(1)).findAllByUser(user);
    }

    @Test
    void testFindAllByUser_IsEmpty() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(likeRepository.findAllByUser(user)).thenReturn(List.of());

        assertThat(likeService.findAllByUser(EMAIL)).isEmpty();

        verify(userRepository, times(1)).findByEmail(EMAIL);
        verify(likeRepository, times(1)).findAllByUser(user);
    }

    @Test
    void testFindAllByPost() {
        when(postRepository.findPostByPostId(String.valueOf(POST_ID))).thenReturn(Optional.of(post));
        when(likeRepository.findAllByPost(post)).thenReturn(List.of(like));

        assertThat(likeService.findAllByPost(String.valueOf(POST_ID)).contains(like));

        verify(postRepository, times(1)).findPostByPostId(String.valueOf(POST_ID));
        verify(likeRepository, times(1)).findAllByPost(post);
    }

    @Test
    void testFindAllByPost_IsEmpty() {
        when(postRepository.findPostByPostId(String.valueOf(POST_ID))).thenReturn(Optional.of(post));
        when(likeRepository.findAllByPost(post)).thenReturn(List.of());

        assertThat(likeService.findAllByPost(String.valueOf(POST_ID))).isEmpty();

        verify(postRepository, times(1)).findPostByPostId(String.valueOf(POST_ID));
        verify(likeRepository, times(1)).findAllByPost(post);
    }

    @Test
    void testFindAllForPost_ZeroParam() {
        when(likeRepository.findAllForPost()).thenReturn(List.of(like));

        assertThat(likeService.findAllForPost()).contains(like);

        verify(likeRepository, times(1)).findAllForPost();
    }

    @Test
    void testFindAllForPost_ZeroParam_IsEmpty() {
        when(likeRepository.findAllForPost()).thenReturn(List.of());

        assertThat(likeService.findAllForPost()).isEmpty();

        verify(likeRepository, times(1)).findAllForPost();
    }

    @Test
    void testFindAllForPost_OneParam() {
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
        when(likeRepository.findAllForPost(post)).thenReturn(List.of(like));

        assertThat(likeService.findAllForPost(Math.toIntExact(POST_ID))).contains(like);

        verify(postRepository, times(1)).findById(POST_ID);
        verify(likeRepository, times(1)).findAllForPost(post);
    }

    @Test
    void testFindAllForPost_OneParam_IsEmpty() {
        when(postRepository.findById(POST_ID)).thenReturn(Optional.of(post));
        when(likeRepository.findAllForPost(post)).thenReturn(List.of());

        assertThat(likeService.findAllForPost(Math.toIntExact(POST_ID))).isEmpty();

        verify(postRepository, times(1)).findById(POST_ID);
        verify(likeRepository, times(1)).findAllForPost(post);
    }

    @Test
    void testFindAllForComments_ZeroParam() {
        when(likeRepository.findAllForComments()).thenReturn(List.of(like));

        assertThat(likeService.findAllForComments()).contains(like);

        verify(likeRepository, times(1)).findAllForComments();
    }

    @Test
    void testFindAllForComments_ZeroParam_IsEmpty() {
        when(likeRepository.findAllForComments()).thenReturn(List.of());

        assertThat(likeService.findAllForComments()).isEmpty();

        verify(likeRepository, times(1)).findAllForComments();
    }

    @Test
    void testFindAllForComments_OneParam() {
        when(commentRepository.findCommentByCommentId(String.valueOf(COMM_ID))).thenReturn(Optional.of(comment));
        when(likeRepository.findAllForCommentsBy(comment)).thenReturn(List.of(like));

        assertThat(likeService.findAllForComments(String.valueOf(Math.toIntExact(COMM_ID)))).contains(like);

        verify(commentRepository, times(1)).findCommentByCommentId(String.valueOf(COMM_ID));
        verify(likeRepository, times(1)).findAllForCommentsBy(comment);
    }

    @Test
    void testFindAllForComments_OneParam_IsEmpty() {
        when(commentRepository.findCommentByCommentId(String.valueOf(COMM_ID))).thenReturn(Optional.of(comment));
        when(likeRepository.findAllForCommentsBy(comment)).thenReturn(List.of());

        assertThat(likeService.findAllForComments(String.valueOf(Math.toIntExact(COMM_ID)))).isEmpty();

        verify(commentRepository, times(1)).findCommentByCommentId(String.valueOf(COMM_ID));
        verify(likeRepository, times(1)).findAllForCommentsBy(comment);
    }

    @Test
    void testFindById() {
        when(likeRepository.findByLikeId(String.valueOf(POST_ID))).thenReturn(Optional.of(like));

        assertThat(likeService.findById(String.valueOf(POST_ID))).contains(like);

        verify(likeRepository).findByLikeId(String.valueOf(POST_ID));
    }

    @Test
    void testFindById_IsEmpty() {
        when(likeRepository.findByLikeId(String.valueOf(POST_ID))).thenReturn(Optional.empty());

        assertThat(likeService.findById(String.valueOf(POST_ID))).isEmpty();

        verify(likeRepository).findByLikeId(String.valueOf(POST_ID));

    }

    @Test
    void testCreateLike() {
        when(userRepository.findByEmail(simplifiedLikeDTO.getUserEmail())).thenReturn(Optional.of(user));
        when(postRepository.findPostByPostId(simplifiedLikeDTO.getPostId())).thenReturn(Optional.of(post));
        when(commentRepository.findCommentByCommentId(simplifiedLikeDTO.getCommentId())).thenReturn(Optional.of(comment));

        when(like.getLikeId()).thenReturn(LIKE_ID);
        when(like.getCreateDate()).thenReturn(date);
        when(like.getUser()).thenReturn(user);
        when(like.getPost()).thenReturn(post);
        when(like.getComment()).thenReturn(comment);

        when(likeRepository.save(any(Like.class))).thenReturn(like);

        assertThat(likeService.createLike(simplifiedLikeDTO))
                .isNotNull()
                .isEqualTo(like)
                .returns(user, Like::getUser)
                .returns(post, Like::getPost)
                .returns(comment, Like::getComment)
                .returns(LIKE_ID,Like::getLikeId)
                .returns(date, Like::getCreateDate);

        verify(userRepository).findByEmail(simplifiedLikeDTO.getUserEmail());
        verify(postRepository).findPostByPostId(simplifiedLikeDTO.getPostId());
        verify(commentRepository).findCommentByCommentId(simplifiedLikeDTO.getCommentId());
        verify(likeRepository).save(any(Like.class));
    }

    @Test
    void testUpdateLike() {
        like.setUpvote(true);

        when(likeRepository.findByLikeId(LIKE_ID)).thenReturn(Optional.of(like));
        when(likeRepository.save(like)).thenReturn(like);

        assertThat(likeService.updateLike(LIKE_ID))
                .containsInstanceOf(Like.class)
                .hasValueSatisfying(l -> assertThat(l.isUpvote()).isFalse());

        verify(likeRepository).findByLikeId(LIKE_ID);
        verify(likeRepository).save(like);
    }

    @Test
    void testUpdateLike_IsEmpty() {
        when(likeRepository.findByLikeId(LIKE_ID)).thenReturn(Optional.empty());

        Optional<Like> result = likeService.updateLike(LIKE_ID);

        assertThat(result).isNotPresent();
        verify(likeRepository).findByLikeId(LIKE_ID);
        verify(likeRepository, never()).save(any(Like.class));
    }

    @Test
    void testDeleteLike() {
        doNothing().when(likeRepository).deleteByLikeId(LIKE_ID);
        likeService.deleteLike(LIKE_ID);
        verify(likeRepository).deleteByLikeId(LIKE_ID);
    }

    @Test
    void testCountAllByPost() {
        when(postRepository.findPostByPostId(LIKE_ID)).thenReturn(Optional.of(post));
        when(likeRepository.countAllByPost(post)).thenReturn(COUNT);

        assertThat(likeService.countAllByPost(LIKE_ID))
                .isEqualTo(COUNT);

        verify(likeRepository).countAllByPost(post);
        verify(postRepository).findPostByPostId(LIKE_ID);
    }

    @Test
    void testCountAllByPost_Null() {
        assertThatThrownBy(() -> likeService.countAllByPost(null))
                .isInstanceOf(NoSuchElementException.class);
    }
}
