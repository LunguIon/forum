package md.forum.forum.repositorys;

import md.forum.forum.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(String name);
}
