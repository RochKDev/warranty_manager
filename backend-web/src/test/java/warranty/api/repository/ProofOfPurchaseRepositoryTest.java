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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// This annotation tells Spring to reset the application context after each test method in the class.
// This will also force Spring to recreate the connection pool, ensuring that no stale or closed connections
// are used across different test methods.
// Reason to use this -> Testcontainers for some reason resets the database container after each test class.
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProofOfPurchaseRepositoryTest extends AbstractTestcontainersTest {

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

        // Create a Product instance
        Product product = Product.builder()
                .name("NVIDIA RTX 3090")
                .description("Gaming graphic card")
                .proofOfPurchase(proofOfPurchase)
                .build();

        // Associate the Product instance with the ProofOfPurchase instance
        proofOfPurchase.setProducts(List.of(product));

        proofOfPurchaseRepository.save(proofOfPurchase);
    }

    @AfterEach
    void tearDown() {
        proofOfPurchaseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldFindByUserId() {
        // When :
        Pageable pageable = Pageable.unpaged();
        Page<ProofOfPurchase> proofOfPurchases = proofOfPurchaseRepository.findByUser_Id(userId, pageable);

        // Then :
        assertEquals(1, proofOfPurchases.getTotalElements());
        assertEquals(SHOP_NAME, proofOfPurchases.getContent().getFirst().getShopName());
        assertEquals(REFERENCE, proofOfPurchases.getContent().getFirst().getReference());
    }

    @Test
    void shouldNotFindByUserId() {
        // When :
        Pageable pageable = Pageable.unpaged();
        Page<ProofOfPurchase> proofOfPurchases = proofOfPurchaseRepository.findByUser_Id(0L, pageable);

        // Then :
        assertEquals(0, proofOfPurchases.getTotalElements());
    }

    @Test
    void shouldFindByShopNameAndReferenceAndUserId() {
        // When :
        Optional<ProofOfPurchase> proofOfPurchase = proofOfPurchaseRepository.findByShopNameAndReferenceAndUser_Id(
                SHOP_NAME, REFERENCE, userId);

        ProofOfPurchase existingProofOfPurchase = proofOfPurchase.get();

        // Then :
        assertNotNull(existingProofOfPurchase);
        assertEquals(SHOP_NAME, existingProofOfPurchase.getShopName());
        assertEquals(REFERENCE, existingProofOfPurchase.getReference());
    }

    @Test
    void shouldNotFindByShopNameAndReferenceAndUserId() {
        // When :
        Optional<ProofOfPurchase> proofOfPurchase = proofOfPurchaseRepository.findByShopNameAndReferenceAndUser_Id(
                "Random Shop Name", "1314848AZAZAZ", userId);

        // Then :
        assertTrue(proofOfPurchase.isEmpty());
    }
}