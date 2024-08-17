package warranty.api.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import warranty.api.exception.ProductNotFoundException;
import warranty.api.exception.ProofOfPurchaseNotFoundException;
import warranty.api.exception.UnauthorizedResourceAccess;
import warranty.api.exception.UserEmailNotFoundException;
import warranty.api.model.Product;
import warranty.api.model.ProofOfPurchase;
import warranty.api.model.User;
import warranty.api.model.dto.ProductDto;
import warranty.api.model.responses.ProductResponseDto;
import warranty.api.repository.ProductRepository;
import warranty.api.repository.ProofOfPurchaseRepository;
import warranty.api.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link ProductServiceImpl} class.
 * This class tests the behavior of methods for handling Product entities.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    private ProductServiceImpl underTest;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProofOfPurchaseRepository proofOfPurchaseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetails userDetails;

    @Captor
    private ArgumentCaptor<Product> productArgumentCaptor;

    /**
     * Set up the test environment by initializing the service with mocked dependencies.
     */
    @BeforeEach
    void setUp() {
        underTest = new ProductServiceImpl(productRepository, proofOfPurchaseRepository, userRepository);
    }

    /**
     * Test the successful saving of a new Product.
     * Ensures the repository's save method is called with the correct arguments.
     */
    @Test
    void shouldSaveProduct() {
        // Given: A user with a valid ProductDto and user details.
        User user = createUserWithId(1L);
        ProductDto productDto = createProductDto();
        ProofOfPurchase proofOfPurchase = createProofOfPurchaseWithUser(user);

        // Mock the user details and repository to return the user.
        mockUserDetails(user);
        mockUserRepository(user);
        mockProofOfPurchaseRepository(proofOfPurchase);

        // Mock the repository to return the saved product.
        Product product = createProductWithDtoAndProofOfPurchase(productDto, proofOfPurchase);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // When: The save method is called. (method under test)
        underTest.save(productDto, userDetails);

        // Then: Verify that the product was saved with the correct data.
        verifyProductSaved(productDto, proofOfPurchase);
    }

    /**
     * Test that a {@link ProofOfPurchaseNotFoundException} is thrown when trying to save a Product
     * with an invalid proof of purchase ID.
     */
    @Test
    void shouldThrowNotFoundExceptionWhenProofOfPurchaseNotFound() {
        // Given: A user with a ProductDto and invalid proof of purchase ID.
        User user = createUserWithId(1L);
        ProductDto productDto = createProductDto();

        // Mock the user details and repository to return the user.
        mockUserDetails(user);
        mockUserRepository(user);

        // Mock the proof of purchase repository to return an empty optional.
        when(proofOfPurchaseRepository.findById(productDto.getProofOfPurchaseId()))
                .thenReturn(Optional.empty());

        // When / Then: Assert that saving a Product with an invalid proof of purchase ID throws a not found exception.
        assertThatThrownBy(() -> underTest.save(productDto, userDetails))
                .isInstanceOf(ProofOfPurchaseNotFoundException.class)
                .hasMessageContaining("Proof of purchase with id");
    }

    /**
     * Test the retrieval of all Products for the current user.
     * Ensures the correct list of ProductResponseDto objects is returned.
     */
    @Test
    void shouldFindAllProducts() {
        // Given: A user with multiple Products.
        User user = createUserWithId(1L);

        // Mock the user details and repository to return the user.
        mockUserDetails(user);
        mockUserRepository(user);

        // Mock the product repository to return a page of products.
        Product product = createProductWithUser(user);
        List<Product> productList = List.of(product);
        Page<Product> page = new PageImpl<>(productList);
        when(productRepository.findByProofOfPurchase_User_Id(anyLong(), any(Pageable.class))).thenReturn(page);

        // When: The findAll method is called. (method under test)
        Page<ProductResponseDto> resultPage = underTest.findAll(userDetails, mock(Pageable.class));

        // Then: Verify that the correct list of ProductResponseDto objects is returned.
        verify(productRepository).findByProofOfPurchase_User_Id(anyLong(), any(Pageable.class));
        ProductResponseDto responseDto = ProductResponseDto.fromEntity(product);
        assertThat(resultPage.getContent()).containsExactly(responseDto);
    }

    /**
     * Test that a {@link ProductNotFoundException} is thrown when a Product
     * is not found by its ID.
     */
    @Test
    void shouldThrowNotFoundExceptionWhenProductNotFoundById() {
        // Given: A non-existing Product ID associated with the user.
        Long id = 1L;
        User user = createUserWithId(1L);

        // Mock the user details and repository to return the user.
        mockUserDetails(user);
        mockUserRepository(user);

        // Mock the repository to return an empty optional.
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // When / Then: Assert that retrieving a non-existing Product throws a not found exception. (method under test)
        assertThatThrownBy(() -> underTest.findOneById(id, userDetails))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("Product with id");
    }

    /**
     * Test the successful retrieval of a single Product by its ID.
     * Ensures the repository's findById method is called with the correct ID.
     */
    @Test
    void shouldFindProductById() {
        // Given: A valid Product ID associated with the user.
        Long id = 1L;
        User user = createUserWithId(1L);

        // Mock the user details and repository to return the user.
        mockUserDetails(user);
        mockUserRepository(user);

        // Mock the repository to return the Product by its ID.
        Product product = createProductWithUser(user);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        // When: The findOneById method is called. (method under test)
        underTest.findOneById(id, userDetails);

        // Then: Verify that the product was retrieved by its ID.
        verify(productRepository).findById(id);
    }

    /**
     * Test that an {@link UnauthorizedResourceAccess} exception is thrown when the user tries
     * to access a Product that they do not own.
     */
    @Test
    void shouldThrowUnauthorizedExceptionWhenUserNotAuthorizedWhenFindById() {
        // Given: A valid Product ID associated with a different user.
        Long id = 1L;
        User user = createUserWithId(1L);
        User differentUser = createUserWithId(2L);

        // Mock the user details and repository to return the user.
        mockUserDetails(user);
        mockUserRepository(user);

        // Mock the repository to return a Product owned by a different user.
        Product product = createProductWithUser(differentUser);  // Different user
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        // When / Then: Assert that accessing a Product not owned by the user throws an unauthorized exception.
        // (method under test)
        assertThatThrownBy(() -> underTest.findOneById(id, userDetails))
                .isInstanceOf(UnauthorizedResourceAccess.class)
                .hasMessageContaining("not authorized");
    }

    /**
     * Test that an {@link UnauthorizedResourceAccess} exception is thrown when the user tries
     * to add a Product to a ProofOfPurchase that they do not own.
     */
    @Test
    void shouldThrowUnauthorizedExceptionWhenUserNotAuthorizedWhenSave() {
        // Given: A user with a ProductDto and a proof of purchase owned by a different user.
        User user = createUserWithId(1L);
        User differentUser = createUserWithId(2L);

        // This Product Dto has the id of the proof of purchase owned by a different user.
        ProductDto productDto = createProductDto();

        // Create a proof of purchase owned by a different user.
        ProofOfPurchase proofOfPurchase = createProofOfPurchaseWithUser(differentUser);

        // Mock the user details and repository to return the user.
        mockUserDetails(user);
        mockUserRepository(user);
        mockProofOfPurchaseRepository(proofOfPurchase);

        // When / Then: Assert that saving a Product to a proof of purchase not owned by the user throws an unauthorized exception.
        assertThatThrownBy(() -> underTest.save(productDto, userDetails))
                .isInstanceOf(UnauthorizedResourceAccess.class)
                .hasMessageContaining("not authorized");
    }

    /**
     * Test the successful deletion of a Product by its ID.
     * Ensures the repository's deleteById method is called with the correct ID.
     */
    @Test
    void shouldDeleteProduct() {
        // Given: A valid Product ID associated with the user.
        Long id = 1L;
        User user = createUserWithId(1L);

        // Mock the user details and repository to return the user.
        mockUserDetails(user);
        mockUserRepository(user);

        // Mock the repository to return the Product by its ID.
        Product product = createProductWithUser(user);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        // When: The deleteById method is called. (method under test)
        underTest.deleteById(id, userDetails);

        // Then: Verify that the product was deleted by its ID.
        verify(productRepository).deleteById(id);
    }

    /**
     * Test the successful updating of a Product.
     * Ensures the repository's save method is called with the updated Product.
     */
    @Test
    void shouldUpdateProduct() {
        // Given: A valid Product ID associated with the user and an updated ProductDto.
        Long id = 1L;
        User user = createUserWithId(1L);

        // Mock the user details and repository to return the user.
        mockUserDetails(user);
        mockUserRepository(user);

        // Mock the repository to return the Product by its ID.
        Product product = createProductWithUser(user);
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        // Create an updated ProductDto and convert it to a Product entity.
        ProductDto updatedProductDto = createUpdatedProductDto();
        ProofOfPurchase proofOfPurchase = createProofOfPurchaseWithUser(user);
        Product updatedProduct = createProductWithDtoAndProofOfPurchase(updatedProductDto, proofOfPurchase);

        // Mock the repository to return the updated Product.
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(proofOfPurchaseRepository.findById(updatedProductDto.getProofOfPurchaseId()))
                .thenReturn(Optional.of(proofOfPurchase));

        // When: The update method is called. (method under test)
        underTest.update(id, updatedProductDto, userDetails);

        // Then: Verify that the product was updated with the correct data.
        verifyProductUpdated(updatedProductDto);
    }

    /**
     * Test that a {@link UserEmailNotFoundException} is thrown when a user cannot be found by their email.
     */
    @Test
    void shouldThrowEmailNotFoundException() {
        // Given: A valid Product ID associated with the user and an updated ProductDto.
        User user = createUserWithId(1L);

        // Mock the user details, but return an empty optional when looking up the user by email.
        mockUserDetails(user);

        // Mock the repository to return an empty optional when looking up the user by email.
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.empty());

        // When / Then: Assert that looking up a user by a non-existing email throws an email not found exception.
        assertThatThrownBy(() -> underTest.save(createProductDto(), userDetails))
                .isInstanceOf(UserEmailNotFoundException.class)
                .hasMessageContaining("User with email");
    }

    /**
     * Mocks the UserDetails to return the provided user's email.
     *
     * @param user the user whose email will be returned by the mock UserDetails.
     */
    private void mockUserDetails(User user) {
        when(userDetails.getUsername()).thenReturn(user.getEmail());
    }

    /**
     * Mocks the UserRepository to return the provided user when looking up by email.
     *
     * @param user the user to be returned by the mock UserRepository.
     */
    private void mockUserRepository(User user) {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    }

    /**
     * Mocks the ProofOfPurchaseRepository to return the provided proof of purchase when looking up by ID.
     *
     * @param proofOfPurchase the proof of purchase to be returned by the mock ProofOfPurchaseRepository.
     */
    private void mockProofOfPurchaseRepository(ProofOfPurchase proofOfPurchase) {
        when(proofOfPurchaseRepository.findById(proofOfPurchase.getId()))
                .thenReturn(Optional.of(proofOfPurchase));
    }

    /**
     * Verifies that the ProductRepository's save method was called with the correct product.
     * Compares the captured product to the expected ProductDto and ProofOfPurchase.
     *
     * @param productDto the expected ProductDto containing product details.
     * @param proofOfPurchase the expected proof of purchase associated with the product.
     */
    private void verifyProductSaved(ProductDto productDto, ProofOfPurchase proofOfPurchase) {
        verify(productRepository).save(productArgumentCaptor.capture());
        Product capturedProduct = productArgumentCaptor.getValue();
        assertThat(capturedProduct.getName()).isEqualTo(productDto.getName());
        assertThat(capturedProduct.getDescription()).isEqualTo(productDto.getDescription());
        assertThat(capturedProduct.getProofOfPurchase()).isEqualTo(proofOfPurchase);
    }

    /**
     * Verifies that the ProductRepository's save method was called with the updated product.
     * Compares the captured product to the expected updated ProductDto.
     *
     * @param updatedProductDto the expected updated ProductDto containing new product details.
     */
    private void verifyProductUpdated(ProductDto updatedProductDto) {
        verify(productRepository).save(productArgumentCaptor.capture());
        Product capturedProduct = productArgumentCaptor.getValue();
        assertThat(capturedProduct.getName()).isEqualTo(updatedProductDto.getName());
        assertThat(capturedProduct.getDescription()).isEqualTo(updatedProductDto.getDescription());
    }

    /**
     * Creates and returns a User with the given ID.
     *
     * @param id the ID of the user to be created.
     * @return the created User with the specified ID.
     */
    private User createUserWithId(Long id) {
        return User.builder()
                .id(id)
                .email("user@example.com")
                .password("password")
                .build();
    }

    /**
     * Creates and returns a Product associated with the provided user.
     * The Product is created using a default ProductDto and a proof of purchase tied to the user.
     *
     * @param user the user to whom the product will be associated.
     * @return the created Product associated with the specified user.
     */
    private Product createProductWithUser(User user) {
        ProofOfPurchase proofOfPurchase = createProofOfPurchaseWithUser(user);
        return createProductWithDtoAndProofOfPurchase(createProductDto(), proofOfPurchase);
    }

    /**
     * Creates and returns a Product using the provided ProductDto and ProofOfPurchase.
     *
     * @param productDto the DTO containing product details.
     * @param proofOfPurchase the proof of purchase associated with the product.
     * @return the created Product using the specified ProductDto and ProofOfPurchase.
     */
    private Product createProductWithDtoAndProofOfPurchase(ProductDto productDto, ProofOfPurchase proofOfPurchase) {
        return Product.builder()
                .id(1L)
                .name(productDto.getName())
                .description(productDto.getDescription())
                .proofOfPurchase(proofOfPurchase)
                .build();
    }

    /**
     * Creates and returns a default ProductDto with pre-set values.
     *
     * @return the created ProductDto with default values.
     */
    private ProductDto createProductDto() {
        return ProductDto.builder()
                .name("Test Product")
                .description("Test Description")
                .proofOfPurchaseId(1L)
                .build();
    }

    /**
     * Creates and returns an updated ProductDto with modified values.
     *
     * @return the created updated ProductDto with new values.
     */
    private ProductDto createUpdatedProductDto() {
        return ProductDto.builder()
                .name("Updated Product")
                .description("Updated Description")
                .proofOfPurchaseId(1L)
                .build();
    }

    /**
     * Creates and returns a ProofOfPurchase associated with the provided user.
     *
     * @param user the user to whom the proof of purchase will be associated.
     * @return the created ProofOfPurchase associated with the specified user.
     */
    private ProofOfPurchase createProofOfPurchaseWithUser(User user) {
        return ProofOfPurchase.builder()
                .id(1L)
                .shopName("Test Shop")
                .reference("Test Reference")
                .buyDate(null)
                .warrantyEndDate(null)
                .description("Test Description")
                .user(user)
                .build();
    }

}
