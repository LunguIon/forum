package md.forum.forum.repositorys;

import md.forum.forum.models.Post;
import md.forum.forum.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAllByUser(User user);
}
