package warranty.api.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Testcontainers;
import warranty.api.AbstractTestcontainersTest;
import warranty.api.model.User;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// This annotation tells Spring to reset the application context after each test method in the class.
// This will also force Spring to recreate the connection pool, ensuring that no stale or closed connections
// are used across different test methods.
// Reason to use this -> Testcontainers for some reason resets the database container after each test class.
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserRepositoryTest extends AbstractTestcontainersTest {

    @Autowired
    private UserRepository userRepository;

    public static final String EMAIL = "roch.the.glock@gmail.com";

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .name("Roch The Glock")
                .email(EMAIL)
                .password("password")
                .build();

        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnUserWhenFindByEmail() {
        // When : Find user by email
        Optional<User> userByEmail = userRepository.findByEmail(EMAIL);
        // Then : User is present
        assertTrue(userByEmail.isPresent());
    }

    @Test
    void shouldNotReturnUserWhenFindByEmail() {
        // When : Find user by email
        Optional<User> userByEmail = userRepository.findByEmail("test@gmail.com");
        // Then : User is not present
        assertThat(userByEmail).isNotPresent();
    }

    @Test
    void shouldReturnTrueWhenExistsByEmail() {
        // When : Check if user exists by email
        boolean existsByEmail = userRepository.existsByEmail(EMAIL);
        // Then : User exists
        assertTrue(existsByEmail);
    }

    @Test
    void shouldReturnFalseWhenExistsByEmail() {
        // When : Check if user exists by email
        boolean existsByEmail = userRepository.existsByEmail("non.existant.email@gmail.com");
        // Then : User does not exist
        assertFalse(existsByEmail);
    }
}