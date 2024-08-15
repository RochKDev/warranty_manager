package warranty.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import warranty.api.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}
