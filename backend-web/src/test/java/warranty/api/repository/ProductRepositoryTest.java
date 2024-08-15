package warranty.api.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import warranty.api.AbstractTestcontainersTest;
import warranty.api.model.Product;
import warranty.api.model.ProofOfPurchase;

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

    private static final String SHOP_NAME = "TopAchat";
    private static final String REFERENCE = "12345ABKUYU878";

    @BeforeEach
    void setUp() {
        // Create a ProofOfPurchase instance
        ProofOfPurchase proofOfPurchase = ProofOfPurchase.builder()
                .shopName(SHOP_NAME)
                .reference(REFERENCE)
                .buyDate(LocalDate.of(2024, 8, 9))
                .warrantyEndDate(LocalDate.of(2026, 8, 9))
                .description("Purchase gaming components")
                .build();
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
    }

    @Test
    void shouldFindByProofOfPurchaseShopNameAndReference() {
        // when
        List<Product> products =
                productRepository.findByProofOfPurchase_ShopNameAndProofOfPurchase_Reference(SHOP_NAME, REFERENCE);

        // then
        assertThat(products).hasSize(1);
        assertThat(products.getFirst().getName()).isEqualTo("NVIDIA RTX 3090");
    }

    @Test
    void shouldNotFindProductIfProofOfPurchaseDoesNotExist() {
        // when
        List<Product> products
                = productRepository.findByProofOfPurchase_ShopNameAndProofOfPurchase_Reference("NonExistentStore", "00000");

        // then
        assertThat(products).isEmpty();
    }
}