package md.forum.forum.repositorys;

import md.forum.forum.models.Like;
import md.forum.forum.models.User;

import java.util.List;

public interface LikeRepository {

    void save(Like like);
    List<Like> findAllByUser(User user);
    List<Like> findAllForPost();
}
