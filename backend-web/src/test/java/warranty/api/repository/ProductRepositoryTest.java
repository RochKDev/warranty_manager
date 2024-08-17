package warranty.api.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Testcontainers;
import warranty.api.AbstractTestcontainersTest;
import warranty.api.model.Product;
import warranty.api.model.ProofOfPurchase;
import warranty.api.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// This annotation tells Spring to reset the application context after each test method in the class.
// This will also force Spring to recreate the connection pool, ensuring that no stale or closed connections
// are used across different test methods.
// Reason to use this -> Testcontainers for some reason resets the database container after each test class.
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProductRepositoryTest extends AbstractTestcontainersTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProofOfPurchaseRepository proofOfPurchaseRepository;

    @Autowired
    private UserRepository userRepository;

    private static final String SHOP_NAME = "MediaMarkt BE";
    private static final String REFERENCE = "12345ABKUYU878";
    private Long userId;

    @BeforeEach
    void setUp() {
        // Create a User instance
        User user = User.builder()
                .name("John Doe")
                .password("123456")
                .email("john.doe@gmail.com")
                .build();

        // Save the User instance and retrieve its ID
        user = userRepository.save(user);
        userId = user.getId();

        // Create a ProofOfPurchase instance
        ProofOfPurchase proofOfPurchase = ProofOfPurchase.builder()
                .shopName(SHOP_NAME)
                .reference(REFERENCE)
                .buyDate(LocalDate.of(2024, 8, 9))
                .warrantyEndDate(LocalDate.of(2026, 8, 9))
                .description("Purchase gaming components")
                .user(user)
                .build();

        // Associate the ProofOfPurchase instance with the User instance
        user.setProofOfPurchases(List.of(proofOfPurchase));
        proofOfPurchaseRepository.save(proofOfPurchase);

        // Create a Product instance
        Product product = Product.builder()
                .name("NVIDIA RTX 3090")
                .description("Gaming graphic card")
                .proofOfPurchase(proofOfPurchase)
                .build();

        productRepository.save(product);
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
        proofOfPurchaseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldFindUserId() {
        // When : Find all products by user ID
        Pageable pageable = Pageable.unpaged();
        Page<Product> products = productRepository.findByProofOfPurchase_User_Id(userId, pageable);

        // Then : Assert that the product is found and the name is correct
        assertThat(products).hasSize(1);
        assertThat(products.getContent().getFirst().getName()).isEqualTo("NVIDIA RTX 3090");
    }

    @Test
    void shouldNotFindByUserId() {
        // When : Find all products by user ID
        Pageable pageable = Pageable.unpaged();
        Page<Product> products = productRepository.findByProofOfPurchase_User_Id(100L, pageable);

        // Then : Assert that the product is not found
        assertThat(products).isEmpty();
    }
}