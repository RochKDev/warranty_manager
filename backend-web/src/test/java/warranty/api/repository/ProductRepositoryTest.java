package warranty.api.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    void shouldFindByProofOfPurchaseAndUserId() {
        // When : Find all products by proof of purchase and user ID
        Pageable pageable = Pageable.unpaged();
        Page<Product> products = productRepository.findByProofOfPurchase_User_Id(userId, pageable);

        // Then : Assert that the product is found and the name is correct
        assertThat(products).hasSize(1);
        assertThat(products.getContent().getFirst().getName()).isEqualTo("NVIDIA RTX 3090");
    }

    @Test
    void shouldNotFindByProofOfPurchaseAndUserId() {
        // When : Find all products by proof of purchase and user ID
        Pageable pageable = Pageable.unpaged();
        Page<Product> products = productRepository.findByProofOfPurchase_User_Id(100L, pageable);

        // Then : Assert that the product is not found
        assertThat(products).isEmpty();
    }
}