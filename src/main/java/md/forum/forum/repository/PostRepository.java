package md.forum.forum.repository;

import md.forum.forum.models.Post;
import md.forum.forum.models.Topic;
import md.forum.forum.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUser(User user);
    List<Post> findAllByTopic(Topic topic);
    Optional<Post> findPostByPostId(String postId);
    boolean existsByPostId(String postId);
    void deleteByPostId(String postId);
}
