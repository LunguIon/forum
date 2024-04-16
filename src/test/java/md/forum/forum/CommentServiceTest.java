package md.forum.forum;

import md.forum.forum.models.Comment;
import md.forum.forum.repositorys.CommentRepository;
import md.forum.forum.services.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(properties = "spring.profiles.active=test")
public class CommentServiceTest {
    public static final String TEST_CONTENT = "Test content";

    @Autowired
    private CommentService commentService;

    private Comment comment;

    @BeforeEach
    public void setUp(){
        comment = createDataForTest();
    }

    @Test
    public void testCreateComment() {
        assertThat(commentService.createComment(comment))
                .isNotNull()
                .returns(TEST_CONTENT, Comment::getContent)
                .extracting(Comment::getId)
                .satisfies(id -> assertThat(id).isGreaterThan(0));
    }

    @Test
    public void testGetAllComments() {
        assertThat(commentService.getAllComments()).isNotNull();
    }

    @Test
    public void testUpdateComment() {
        comment = createDataForTest();
        commentService.createComment(comment);
        Comment newComment = new Comment();
        newComment.setContent("Test to update");
        newComment.setValue_of_like(1);
        newComment.setCreate_date(new Date(System.currentTimeMillis()));

        comment = commentService.updateComment((long) comment.getId(), newComment);

        assertThat(comment).isNotNull();
    }

    @Test
    public void testDeleteComment() {
        Comment comment = createDataForTest();
        boolean deletedComment = commentService.deleteComment((long) comment.getId());
        assertThat(deletedComment).isFalse();
    }

    private Comment createDataForTest() {
        Comment comment = new Comment();
        comment.setContent(TEST_CONTENT);
        comment.setValue_of_like(1);
        comment.setCreate_date(new Date(System.currentTimeMillis()));
        return comment;
    }
}
