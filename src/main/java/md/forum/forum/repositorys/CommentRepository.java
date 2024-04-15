package md.forum.forum.repositorys;

import md.forum.forum.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    void deleteAllByPostId(Long postId);
}
