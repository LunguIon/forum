package md.forum.forum.services;

import md.forum.forum.dto.get.GetCommentDTO;
import md.forum.forum.dto.mappers.CommentDTOMapper;
import md.forum.forum.dto.simplified.SimplifiedCommentDTO;
import md.forum.forum.models.Comment;
import md.forum.forum.models.Post;
import md.forum.forum.models.User;
import md.forum.forum.repository.CommentRepository;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest implements WithAssertions {
    public static final String TEST_CONTENT = "Test content";
    public static final String TEST_TO_UPDATE = "Test to update";
    public static final long ID_LONG = 69L;
    public static final int ID_INT = 69;
    public static final String ID_STRING="65";
    public static final String EMAIL = "email/";
    @Mock
    CommentRepository commentRepository;
    @Mock
    Comment comment;
    @Mock
    Comment commentSecond;
    @Mock
    GetCommentDTO dto1, dto2;
    @Mock
    User user;
    @Mock
    Post post;
    @Mock
    CommentDTOMapper commentDTOMapper;
    @Mock
    Date date;
    @Mock
    SimplifiedCommentDTO simplifiedCommentDTO;
    @Mock
    UserService userService;
    @Mock
    PostService postService;
    @InjectMocks
    CommentService commentService;

    @Test
    void testCreateComment() {
        when(comment.getId()).thenReturn(ID_INT);
        when(comment.getContent()).thenReturn(TEST_CONTENT);
        when(commentRepository.save(comment)).thenReturn(comment);

        assertThat(commentService.createComment(comment))
                .isNotNull()
                .returns(TEST_CONTENT, Comment::getContent)
                .returns(ID_INT, Comment::getId);

        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testGetAllComments() {
        when(commentRepository.findAll()).thenReturn(Arrays.asList(comment, commentSecond));
        when(commentDTOMapper.apply(comment)).thenReturn(dto1);
        when(commentDTOMapper.apply(commentSecond)).thenReturn(dto2);

        assertThat(commentService.getAllComments()).isEqualTo(Arrays.asList(dto1, dto2));

        verify(commentRepository).findAll();
        verify(commentDTOMapper).apply(comment);
        verify(commentDTOMapper).apply(commentSecond);
    }

    @Test
    void testGetAllComments_IsNull() { assertThat(commentService.getAllComments()).isEqualTo(List.of()); }

    @Test
    void testGetCommentByCommentId() {
        when(commentRepository.findCommentByCommentId(ID_STRING)).thenReturn(Optional.of(comment));
        when(commentDTOMapper.apply(comment)).thenReturn(dto1);
        when(dto1.content()).thenReturn(TEST_CONTENT);

        assertThat(commentService.getCommentByCommentId(ID_STRING))
                .contains(dto1)
                .map(GetCommentDTO::content)
                .contains(TEST_CONTENT);

        verify(commentRepository).findCommentByCommentId(ID_STRING);
        verify(commentDTOMapper).apply(comment);
    }

    @Test
    void testGetCommentByCommentId_NotFound() { assertThat(commentService.getCommentByCommentId(ID_STRING)).isEqualTo(Optional.empty()); }

    @Test
    void testGetCommentByCommentIdFull() {
        when(commentRepository.findCommentByCommentId(ID_STRING)).thenReturn(Optional.of(comment));

        assertThat(commentService.getCommentByCommentIdFull(ID_STRING))
                .contains(comment);

        verify(commentRepository).findCommentByCommentId(ID_STRING);
    }

    @Test
    void testGetCommentByCommentIdFull_IsEmpty() { assertThat(commentService.getCommentByCommentIdFull(ID_STRING)).isEmpty();}

    @Test
    void testGetCommentById() {
        when(commentRepository.findById(ID_LONG)).thenReturn(Optional.of(comment));

        assertThat(commentService.getCommentById(ID_LONG)).contains(comment);

        verify(commentRepository).findById(ID_LONG);
    }

    @Test
    void testGetCommentById_NotFound() {
        assertThat(commentService.getCommentById((long) comment.getId())).isEmpty();
    }

    @Test
    void testGetCommentsByPostId() {
        when(commentRepository.findAllByPostPostId(ID_STRING)).thenReturn(List.of(comment));
        when(commentDTOMapper.apply(comment)).thenReturn(dto1);

        assertThat(commentService.getCommentsByPostId(ID_STRING))
                .contains(dto1);

        verify(commentRepository).findAllByPostPostId(ID_STRING);
        verify(commentDTOMapper).apply(comment);
    }

    @Test
    void testGetCommentsByPostId_IsEmpty() {
        assertThat(commentService.getCommentsByPostId(ID_STRING)).isEmpty();
    }

    @Test
    void testCreateCommentDTO() {
        when(simplifiedCommentDTO.getContent()).thenReturn(TEST_CONTENT);
        when(simplifiedCommentDTO.getEmail()).thenReturn(EMAIL);
        when(simplifiedCommentDTO.getPostId()).thenReturn(ID_STRING);
        when(userService.getUserByEmailFull(simplifiedCommentDTO.getEmail())).thenReturn(Optional.of(user));
        when(postService.getPostByPostIdFull(simplifiedCommentDTO.getPostId())).thenReturn(Optional.of(post));

        when(comment.getCommentId()).thenReturn(ID_STRING);
        when(comment.getContent()).thenReturn(TEST_CONTENT);
        when(comment.getCreationDate()).thenReturn(date);
        when(comment.getUser()).thenReturn(user);
        when(comment.getPost()).thenReturn(post);

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        assertThat(commentService.createComment(simplifiedCommentDTO))
                .isNotNull()
                .isEqualTo(comment)
                .returns(TEST_CONTENT, Comment::getContent)
                .returns(date, Comment::getCreationDate)
                .returns(ID_STRING, Comment::getCommentId)
                .returns(user, Comment::getUser)
                .returns(post, Comment::getPost);

        verify(commentRepository).save(any(Comment.class));
        verify(userService).getUserByEmailFull(simplifiedCommentDTO.getEmail());
        verify(postService).getPostByPostIdFull(simplifiedCommentDTO.getPostId());
    }

    @Test
    void testUpdateCommentDTO() {
        when(simplifiedCommentDTO.getContent()).thenReturn(TEST_CONTENT);
        when(commentRepository.findCommentByCommentId(ID_STRING)).thenReturn(Optional.of(comment));
        when(comment.getContent()).thenReturn(TEST_CONTENT);
        when(comment.getUpdateDate()).thenReturn(date);

        when(commentRepository.save(comment)).thenReturn(comment);

        assertThat(commentService.updateComment(ID_STRING, simplifiedCommentDTO))
                .isNotNull()
                .returns(TEST_CONTENT, Comment::getContent)
                .returns(date, Comment::getUpdateDate);

        verify(commentRepository).findCommentByCommentId(ID_STRING);
        verify(commentRepository).save(comment);
    }

    @Test
    void testUpdateCommentDTO_IsNull() {
        assertThat(commentService.updateComment(ID_STRING, simplifiedCommentDTO)).isNull();
    }

    @Test
    void testUpdateComment() {
        when(commentRepository.findById(ID_LONG)).thenReturn(Optional.of(comment));
        when(commentSecond.getContent()).thenReturn(TEST_TO_UPDATE);
        when(commentSecond.getNumberOfLikes()).thenReturn(2);
        when(commentSecond.getUpdateDate()).thenReturn(date);

        when(commentRepository.save(comment)).thenReturn(comment);

        assertThat(commentService.updateComment(ID_LONG, commentSecond))
                .isEqualTo(comment);

        verify(commentRepository).findById(ID_LONG);
        verify(commentRepository).save(comment);
    }

    @Test
    void testUpdateComment_EmptyId() {
        when(commentRepository.findById(ID_LONG)).thenReturn(Optional.empty());

        assertThat(commentService.updateComment(ID_LONG, commentSecond))
                .isNull();

        verify(commentRepository).findById(ID_LONG);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void testUpdateComment_NullId() {
        assertThat(commentService.updateComment(null, commentSecond))
                .isNull();
    }

    @Test
    void testUpdateComment_NullNewComment() {
        when(commentRepository.findById(ID_LONG)).thenReturn(Optional.of(comment));

        assertThatThrownBy(() -> commentService.updateComment(ID_LONG, null))
                .isInstanceOf(NullPointerException.class);

        verify(commentRepository).findById(ID_LONG);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    void testDeleteComment() {
        when(commentRepository.existsById(ID_LONG)).thenReturn(true);
        doNothing().when(commentRepository).deleteById(ID_LONG);

        assertThat(commentService.deleteComment(ID_LONG)).isTrue();

        verify(commentRepository).deleteById(ID_LONG);
    }

    @Test
    void testDeleteComment_NotExist() {
        assertThat(commentService.deleteComment(ID_LONG)).isFalse();
    }

    @Test
    void testDeleteCommentByCommentId() {
        when(commentRepository.existsCommentByCommentId(ID_STRING)).thenReturn(true);
        doNothing().when(commentRepository).deleteCommentByCommentId(ID_STRING);

        assertThat(commentService.deleteComment(ID_STRING)).isTrue();

        verify(commentRepository).existsCommentByCommentId(ID_STRING);
    }

    @Test
    void testDeleteCommentByCommentId_NonExist() { assertThat(commentService.deleteComment(ID_STRING)).isFalse(); }

    @Test
    void testCountAllByPostId() {
        when(commentRepository.countAllByPostPostId(ID_STRING)).thenReturn(ID_INT);

        assertThat(commentService.countAllByPostId(ID_STRING)).isEqualTo(ID_INT);

        verify(commentRepository).countAllByPostPostId(ID_STRING);
    }

    @Test
    void testCountAllByPostId_IsNull() {
        assertThat(commentService.countAllByPostId(null)).isZero();
    }
}
