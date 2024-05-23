package md.forum.forum.repository;

import md.forum.forum.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    void deleteByEmail(String email);
    @Transactional
    @Modifying
    @Query("UPDATE users u SET u.imageUrlProfile= :imageUrl WHERE u.email = :email")
    void updateUserImageUrl(@Param("email")String email,@Param("imageUrl") String imageUrl);

}

