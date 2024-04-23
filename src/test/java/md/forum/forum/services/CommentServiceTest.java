package md.forum.forum.services;

import md.forum.forum.models.Comment;
import md.forum.forum.repositorys.CommentRepository;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest implements WithAssertions {
    public static final String TEST_CONTENT = "Test content";
    public static final String TEST_TO_UPDATE = "Test to update";
    public static final long ID_LONG = 69L;
    public static final int ID_INT = 69;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private Comment comment;
    @Mock
    private Comment commentSecond;
    @InjectMocks
    private CommentService commentService;
    @Mock
    private Date date;

    @Test
    public void testCreateComment() {
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
    public void testGetAllComments() {
        when(commentRepository.findAll()).thenReturn(List.of(comment, commentSecond));

        assertThat(commentService.getAllComments()).hasSize(2)
                .contains(commentSecond, comment);

        verify(commentRepository, times(1)).findAll();
    }

    @Test
    public void testGetCommentById_Found() {
        when(comment.getId()).thenReturn(ID_INT);
        when(comment.getContent()).thenReturn(TEST_CONTENT);
        when(commentRepository.findById((long) comment.getId())).thenReturn(Optional.of(comment));

        Optional<Comment> foundComment = commentService.getCommentById((long) comment.getId());
        assertThat(foundComment)
                .isPresent()
                .get()
                .extracting(Comment::getContent)
                .isEqualTo(TEST_CONTENT);

        verify(commentRepository, times(1)).findById((long) comment.getId());
    }

    @Test void testGetCommentById_NotFound() {
        Optional<Comment> foundComment = commentService.getCommentById((long) comment.getId());
        assertThat(foundComment).isEmpty();
    }

    @Test
    public void testUpdateComment() {
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
    public void testUpdateComment_EmptyId() {
        when(commentRepository.findById(ID_LONG)).thenReturn(Optional.empty());

        assertThat(commentService.updateComment(ID_LONG, commentSecond))
                .isNull();

        verify(commentRepository).findById(ID_LONG);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    public void testUpdateComment_NullId() {
        assertThat(commentService.updateComment(null, commentSecond))
                .isNull();

        verify(commentRepository).findById(null);
    }

    @Test
    public void testUpdateComment_NullNewComment() {
        when(commentRepository.findById(ID_LONG)).thenReturn(Optional.of(comment));

        assertThatThrownBy(()->commentService.updateComment(ID_LONG, null))
                .isInstanceOf(NullPointerException.class);

        verify(commentRepository).findById(ID_LONG);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    public void testDeleteComment() {
        when(commentRepository.existsById(ID_LONG)).thenReturn(true);
        doNothing().when(commentRepository).deleteById(ID_LONG);

        assertThat(commentService.deleteComment(ID_LONG)).isTrue();

        verify(commentRepository).deleteById(ID_LONG);
    }

    @Test
    public void testDeleteComment_NotExist() {
        when(commentRepository.existsById(ID_LONG)).thenReturn(false);

        assertThat(commentService.deleteComment(ID_LONG)).isFalse();

        verify(commentRepository, never()).deleteById(ID_LONG);
    }
}
