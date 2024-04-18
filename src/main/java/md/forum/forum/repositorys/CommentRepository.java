package md.forum.forum.repositorys;

import md.forum.forum.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostId(Long postId);
    void deleteAllByPostId(Long postId);
}
