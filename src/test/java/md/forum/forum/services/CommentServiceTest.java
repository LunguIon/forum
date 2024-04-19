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
    public static final long ID = 69L;

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
        when(comment.getId()).thenReturn(69);
        when(comment.getContent()).thenReturn(TEST_CONTENT);
        when(commentRepository.save(comment)).thenReturn(comment);

        assertThat(commentService.createComment(comment))
                .isNotNull()
                .returns(TEST_CONTENT, Comment::getContent)
                .returns(69, Comment::getId);

        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    public void testGetAllComments() {
        when(commentRepository.findAll()).thenReturn(List.of(comment, commentSecond));

        List<Comment> allComments = commentService.getAllComments();
        assertThat(allComments).hasSize(2)
                .contains(commentSecond, comment);

        verify(commentRepository, times(1)).findAll();
    }

    @Test
    public void testGetCommentByIdFound() {
        when(comment.getId()).thenReturn(69);
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

    @Test void testGetCommentByIdNotFound() {
        Optional<Comment> foundComment = commentService.getCommentById((long) comment.getId());
        assertThat(foundComment).isEmpty();
    }

    @Test
    public void testUpdateComment() {
        when(commentRepository.findById(ID)).thenReturn(Optional.of(comment));
        when(commentSecond.getContent()).thenReturn(TEST_TO_UPDATE);
        when(commentSecond.getValue_of_like()).thenReturn(2);
        when(commentSecond.getUpdate_date()).thenReturn(date);

        when(commentRepository.save(comment)).thenReturn(comment);

        assertThat(commentService.updateComment(ID, commentSecond))
                .isEqualTo(comment);

        verify(commentRepository).findById(ID);
        verify(commentRepository).save(comment);
    }

    @Test
    public void testUpdateCommentWithEmpty() {
        when(commentRepository.findById(ID)).thenReturn(Optional.empty());

        assertThat(commentService.updateComment(ID, commentSecond))
                .isNull();

        verify(commentRepository).findById(ID);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    public void testUpdateCommentWithNullId() {
        assertThat(commentService.updateComment(null, commentSecond))
                .isNull();

        verify(commentRepository).findById(null);
    }

    @Test
    public void testUpdateCommentWithNullNewComment() {
        when(commentRepository.findById(ID)).thenReturn(Optional.of(comment));

        assertThatThrownBy(()->commentService.updateComment(ID, null))
                .isInstanceOf(NullPointerException.class);

        verify(commentRepository).findById(ID);
        verifyNoMoreInteractions(commentRepository);
    }

    @Test
    public void testDeleteComment() {
        when(commentRepository.existsById(ID)).thenReturn(true);
        doNothing().when(commentRepository).deleteById(ID);

        assertThat(commentService.deleteComment(ID)).isTrue();

        verify(commentRepository).deleteById(ID);
    }

    @Test
    public void testDeleteNonExistentComment() {
        when(commentRepository.existsById(ID)).thenReturn(false);

        assertThat(commentService.deleteComment(ID)).isFalse();

        verify(commentRepository, never()).deleteById(ID);
    }
}
