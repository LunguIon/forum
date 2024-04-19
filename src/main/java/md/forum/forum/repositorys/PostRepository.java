package md.forum.forum.repositorys;

import md.forum.forum.models.Post;
import md.forum.forum.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAllByUser(User user);
}
