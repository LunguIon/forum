package md.forum.forum.repository;

import md.forum.forum.models.Comment;
import md.forum.forum.models.Like;
import md.forum.forum.models.Post;
import md.forum.forum.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findAllByUser(User user);

    List<Like> findAllByPost(Post post);

    int countAllByPost(Post post);

    @Query("SELECT l FROM likes l where l.comment is null")
    List<Like> findAllForPost();

    @Query("SELECT l FROM likes l where l.comment is null and l.post = :post")
    List<Like> findAllForPost(@Param("post") Post post);

    @Query("SELECT l FROM likes l where l.comment is not null")
    List<Like> findAllForComments();

    @Query("SELECT l FROM likes l WHERE l.comment IS NOT NULL AND l.comment = :comment")
    List<Like> findAllForCommentsBy(@Param("comment") Comment comment);

    //UUID
    Optional<Like> findByLikeId(String likeId);
    void deleteByLikeId(String likeId);
}
