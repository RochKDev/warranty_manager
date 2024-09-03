package warranty.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import warranty.api.model.User;

import java.util.Optional;

/**
 * This interface is responsible for managing the user data.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find a user by email.
     *
     * @param email The email of the user.
     * @return The optional user.
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user exists by email.
     *
     * @param email The email of the user.
     * @return True if the user exists, false otherwise.
     */
    Boolean existsByEmail(String email);
}
