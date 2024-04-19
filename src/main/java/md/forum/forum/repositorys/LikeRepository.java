package md.forum.forum.repositorys;

import md.forum.forum.models.Comment;
import md.forum.forum.models.Like;
import md.forum.forum.models.Post;
import md.forum.forum.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository {

    List<Like> findAllByUser(User user);
    List<Like> findAllByPost(Post post);
    @Query("SELECT l FROM likes l where l.comments = null")
    List<Like> findAllForPost();
    @Query("SELECT l FROM likes l where l.comments = null ")
    List<Like> findAllForPost(Post post);
    @Query("SELECT l FROM likes l where l.comments != null")
    List<Like> findAllForComments();
    @Query("SELECT l FROM likes l where l.comments != null and l.comments = :coment")
    List<Like> findAllForComments(Comment comment);
    @Query("SELECT l FROM likes l where l.comments != null and l.comments = :coment and l.post = :post")
    List<Like> findAllForComments(Comment comment, Post post);
}
