package md.forum.forum.repository;

import md.forum.forum.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostPostId(String postPostId);
    int countAllByPostPostId(String postPostId);
    void deleteAllByPostPostId(String postPostId);
    // special UUID
    void deleteCommentByCommentId(String commentId);
    Optional<Comment> findCommentByCommentId(String commentId);
    boolean existsCommentByCommentId(String commentId);
}
