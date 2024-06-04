package md.forum.forum.repository;

import md.forum.forum.models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TopicRepository extends JpaRepository<Topic,Integer> {
    Optional<Topic> findById(int id);
    Optional<Topic> findByTitle(String title);
    List<Topic> findByUserEmail(String email);
    List<Topic> findAllByOrderByTitleDesc();
    void deleteByTitle(String title);
}
