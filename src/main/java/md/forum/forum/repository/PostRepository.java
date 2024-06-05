package md.forum.forum.repository;

import jakarta.transaction.Transactional;
import md.forum.forum.models.Post;
import md.forum.forum.models.Topic;
import md.forum.forum.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    //for image
    @Transactional
    @Modifying
    @Query("update posts p set p.imageURL = :imageUrl where p.postId = :postId")
    void updateImageByPostId(@Param("postId") String postId,@Param("imageUrl") String imageUrl);

}
