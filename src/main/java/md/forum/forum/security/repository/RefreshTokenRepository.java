package md.forum.forum.security.repository;

import md.forum.forum.models.User;
import md.forum.forum.security.model.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);
    boolean existsByUser(User user);
    Optional<RefreshToken> findByUser(User user);
    void deleteByUser(User user);
}
