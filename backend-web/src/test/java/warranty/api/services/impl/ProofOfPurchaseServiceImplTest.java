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
import warranty.api.exception.ProofOfPurchaseConflictException;
import warranty.api.exception.ProofOfPurchaseNotFoundException;
import warranty.api.exception.UnauthorizedResourceAccess;
import warranty.api.exception.UserEmailNotFoundException;
import warranty.api.model.Product;
import warranty.api.model.ProofOfPurchase;
import warranty.api.model.User;
import warranty.api.model.dto.ProofOfPurchaseDto;
import warranty.api.model.dto.ProductDto;
import warranty.api.model.responses.ProofOfPurchaseResponseDto;
import warranty.api.repository.ProofOfPurchaseRepository;
import warranty.api.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link ProofOfPurchaseServiceImpl} class.
 * This class tests the behavior of methods for handling Proof of Purchase entities.
 */
@ExtendWith(MockitoExtension.class)
class ProofOfPurchaseServiceImplTest {

    private ProofOfPurchaseServiceImpl underTest;

    @Mock
    private ProofOfPurchaseRepository proofOfPurchaseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetails userDetails;

    @Captor
    private ArgumentCaptor<ProofOfPurchase> proofOfPurchaseArgumentCaptor;

    /**
     * Set up the test environment by initializing the service with mocked dependencies.
     */
    @BeforeEach
    void setUp() {
        underTest = new ProofOfPurchaseServiceImpl(proofOfPurchaseRepository, userRepository);
    }

    /**
     * Test the successful saving of a new Proof of Purchase.
     * Ensures the repository's save method is called with the correct arguments.
     */
    @Test
    void shouldSaveProofOfPurchase() {
        // Given: A user with a valid ProofOfPurchaseDto and user details.
        User user = createUserWithId(1L);
        ProofOfPurchaseDto proofOfPurchaseDto = createProofOfPurchaseDto();

        // Mock the user details and repository to return the user.
        mockUserDetails(user);
        mockUserRepository(user);

        // Convert the DTO to a ProofOfPurchase entity and mock the repository to return the saved entity.
        ProofOfPurchase proofOfPurchase = createProofOfPurchaseWithDtoAndUser(proofOfPurchaseDto, user);
        when(proofOfPurchaseRepository.save(any(ProofOfPurchase.class))).thenReturn(proofOfPurchase);

        // When: The save method is called. (method under test)
        underTest.save(proofOfPurchaseDto, userDetails);

        // Then: Verify that the proof of purchase was saved with the correct data.
        verifyProofOfPurchaseSaved(proofOfPurchaseDto, user);
    }

    /**
     * Test that a {@link ProofOfPurchaseConflictException} is thrown when attempting to save
     * a duplicate Proof of Purchase.
     */
    @Test
    void shouldThrowConflictExceptionWhenSavingDuplicateProofOfPurchase() {
        // Given: A user and an existing ProofOfPurchase with the same data.
        User user = createUserWithId(1L);
        ProofOfPurchaseDto proofOfPurchaseDto = createProofOfPurchaseDto();

        // Mock the user details and repository to return the user.
        mockUserDetails(user);
        mockUserRepository(user);

        // Mock the repository to return an existing ProofOfPurchase.
        mockExistingProofOfPurchase();

        // When / Then: Assert that saving a duplicate ProofOfPurchase throws a conflict exception. (method under test)
        assertThatThrownBy(() -> underTest.save(proofOfPurchaseDto, userDetails))
                .isInstanceOf(ProofOfPurchaseConflictException.class)
                .hasMessageContaining("already exists");
    }

    /**
     * Test the retrieval of all Proof of Purchase records for the current user.
     * Ensures the correct list of ProofOfPurchaseResponseDto objects is returned.
     */
    @Test
    void shouldFindAllProofsOfPurchase() {
        // Given: A user with multiple ProofOfPurchase records.
        User user = createUserWithId(1L);

        // Mock the user details and repository to return the user.
        mockUserDetails(user);
        mockUserRepository(user);

        // Mock the repository to return a page of ProofOfPurchase records.
        ProofOfPurchase proof1 = createProofOfPurchaseWithUser(user);
        List<ProofOfPurchase> proofList = List.of(proof1);
        Page<ProofOfPurchase> page = new PageImpl<>(proofList);
        when(proofOfPurchaseRepository.findByUser_Id(anyLong(), any(Pageable.class))).thenReturn(page);

        // When: The findAll method is called. (method under test)
        Page<ProofOfPurchaseResponseDto> resultPage = underTest.findAll(mock(Pageable.class), userDetails);

        // Then: Verify that the correct list of ProofOfPurchaseResponseDto objects is returned.
        verify(proofOfPurchaseRepository).findByUser_Id(anyLong(), any(Pageable.class));
        ProofOfPurchaseResponseDto responseDto1 = ProofOfPurchaseResponseDto.fromEntity(proof1);
        assertThat(resultPage.getContent()).containsExactly(responseDto1);
    }

    /**
     * Test that a {@link ProofOfPurchaseNotFoundException} is thrown when a Proof of Purchase
     * is not found by its ID.
     */
    @Test
    void shouldThrowNotFoundExceptionWhenProofOfPurchaseNotFoundById() {
        // Given: A non-existing ProofOfPurchase ID.
        Long id = 1L;

        // Mock the repository to return an empty optional.
        when(proofOfPurchaseRepository.findById(id)).thenReturn(Optional.empty());

        // When / Then: Assert that retrieving a non-existing ProofOfPurchase throws a not found exception. (method under test)
        assertThatThrownBy(() -> underTest.findOneById(id, userDetails))
                .isInstanceOf(ProofOfPurchaseNotFoundException.class)
                .hasMessageContaining("not found");
    }

    /**
     * Test the successful retrieval of a single Proof of Purchase by its ID.
     * Ensures the repository's findById method is called with the correct ID.
     */
    @Test
    void shouldFindProofOfPurchaseById() {
        // Given: A valid ProofOfPurchase ID associated with the user.
        Long id = 1L;
        User user = createUserWithId(1L);

        // Mock the user details and repository to return the user.
        mockUserDetails(user);
        mockUserRepository(user);

        // Mock the repository to return the ProofOfPurchase by its ID.
        ProofOfPurchase proofOfPurchase = createProofOfPurchaseWithUser(user);
        when(proofOfPurchaseRepository.findById(id)).thenReturn(Optional.of(proofOfPurchase));

        // When: The findOneById method is called. (method under test)
        underTest.findOneById(id, userDetails);

        // Then: Verify that the proof of purchase was retrieved by its ID.
        verify(proofOfPurchaseRepository).findById(id);
    }

    /**
     * Test that an {@link UnauthorizedResourceAccess} exception is thrown when the user tries
     * to access a Proof of Purchase that they do not own.
     */
    @Test
    void shouldThrowUnauthorizedExceptionWhenUserNotAuthorized() {
        // Given: A valid ProofOfPurchase ID associated with a different user.
        Long id = 1L;
        User user = createUserWithId(1L);
        User differentUser = createUserWithId(2L);

        // Mock the user details and repository to return the user.
        mockUserDetails(user);
        mockUserRepository(user);

        // Mock the repository to return a ProofOfPurchase owned by a different user.
        ProofOfPurchase proofOfPurchase = createProofOfPurchaseWithUser(differentUser);  // Different user
        when(proofOfPurchaseRepository.findById(id)).thenReturn(Optional.of(proofOfPurchase));

        // When / Then: Assert that accessing a ProofOfPurchase not owned by the user throws an unauthorized exception.
        // (method under test)
        assertThatThrownBy(() -> underTest.findOneById(id, userDetails))
                .isInstanceOf(UnauthorizedResourceAccess.class)
                .hasMessageContaining("not authorized");
    }

    /**
     * Test the successful deletion of a Proof of Purchase by its ID.
     * Ensures the repository's deleteById method is called with the correct ID.
     */
    @Test
    void shouldDeleteProofOfPurchase() {
        // Given: A valid ProofOfPurchase ID associated with the user.
        Long id = 1L;
        User user = createUserWithId(1L);

        // Mock the user details and repository to return the user.
        mockUserDetails(user);
        mockUserRepository(user);

        // Mock the repository to return the ProofOfPurchase by its ID.
        ProofOfPurchase proofOfPurchase = createProofOfPurchaseWithUser(user);
        when(proofOfPurchaseRepository.findById(id)).thenReturn(Optional.of(proofOfPurchase));

        // When: The deleteById method is called. (method under test)
        underTest.deleteById(id, userDetails);

        // Then: Verify that the proof of purchase was deleted by its ID.
        verify(proofOfPurchaseRepository).deleteById(id);
    }

    /**
     * Test the successful updating of a Proof of Purchase.
     * Ensures the repository's save method is called with the updated Proof of Purchase.
     */
    @Test
    void shouldUpdateProofOfPurchase() {
        // Given: A valid ProofOfPurchase ID associated with the user and an updated ProofOfPurchaseDto.
        Long id = 1L;
        User user = createUserWithId(1L);

        // Mock the user details and repository to return the user.
        mockUserDetails(user);
        mockUserRepository(user);

        // Mock the repository to return the ProofOfPurchase by its ID.
        ProofOfPurchase proofOfPurchase = createProofOfPurchaseWithUser(user);
        when(proofOfPurchaseRepository.findById(id)).thenReturn(Optional.of(proofOfPurchase));

        // Create an updated ProofOfPurchaseDto and convert it to a ProofOfPurchase entity.
        ProofOfPurchaseDto updatedProofOfPurchaseDto = createUpdatedProofOfPurchaseDto();
        ProofOfPurchase updatedProofOfPurchase = createProofOfPurchaseWithDtoAndUser(updatedProofOfPurchaseDto, user);

        // Mock the repository to return the updated ProofOfPurchase.
        when(proofOfPurchaseRepository.save(any(ProofOfPurchase.class))).thenReturn(updatedProofOfPurchase);

        // When: The update method is called. (method under test)
        underTest.update(id, updatedProofOfPurchaseDto, userDetails);

        // Then: Verify that the proof of purchase was updated with the correct data.
        verifyProofOfPurchaseUpdated(updatedProofOfPurchaseDto);
    }

    /**
     * Test that a {@link UserEmailNotFoundException} is thrown when a user cannot be found by their email.
     */
    @Test
    void shouldThrowEmailNotFoundException() {
        // Given: A non-existing email address.

        // Mock the user details and repository to return an empty optional.
        when(userDetails.getUsername()).thenReturn("nonexistent@example.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // When / Then: Assert that saving a ProofOfPurchase with a non-existing email throws an email not found exception.
        // (method under test)
        assertThatThrownBy(() -> underTest.save(mock(ProofOfPurchaseDto.class), userDetails))
                .isInstanceOf(UserEmailNotFoundException.class)
                .hasMessageContaining("not found");
    }

    // Helper methods

    /**
     * Helper method to create a {@link User} with the specified ID.
     *
     * @param id the ID to assign to the user
     * @return a new {@link User} instance with the given ID
     */
    private User createUserWithId(Long id) {
        return User.builder().
                id(id)
                .email("user@example.com")
                .password("password")
                .build();
    }

    /**
     * Helper method to create a {@link ProofOfPurchaseDto} instance for testing.
     *
     * @return a new {@link ProofOfPurchaseDto} instance with sample data
     */
    private ProofOfPurchaseDto createProofOfPurchaseDto() {
        return new ProofOfPurchaseDto(
                "ShopName",
                "Reference",
                LocalDate.now(),
                LocalDate.now().plusYears(2),
                "Description",
                List.of(new ProductDto("Product1", "Product Description"))
        );
    }

    /**
     * Helper method to create an updated {@link ProofOfPurchaseDto} instance for testing.
     *
     * @return a new {@link ProofOfPurchaseDto} instance with updated data
     */
    private ProofOfPurchaseDto createUpdatedProofOfPurchaseDto() {
        return new ProofOfPurchaseDto(
                "New ShopName",
                "New Reference",
                LocalDate.now(),
                LocalDate.now().plusYears(2),
                "New Description",
                List.of(new ProductDto("Product1", "Product Description"))
        );
    }

    /**
     * Helper method to mock the {@link UserDetails} with the specified {@link User}.
     *
     * @param user the {@link User} to mock the {@link UserDetails} with
     */
    private void mockUserDetails(User user) {
        when(userDetails.getUsername()).thenReturn(user.getEmail());
    }

    /**
     * Helper method to mock the {@link UserRepository} to return the specified {@link User}.
     *
     * @param user the {@link User} to return from the repository
     */
    private void mockUserRepository(User user) {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
    }

    /**
     * Helper method to mock an existing {@link ProofOfPurchase} in the repository.
     */
    private void mockExistingProofOfPurchase() {
        when(proofOfPurchaseRepository.findByShopNameAndReferenceAndUser_Id(anyString(), anyString(), anyLong()))
                .thenReturn(Optional.of(new ProofOfPurchase()));
    }

    /**
     * Helper method to create a {@link ProofOfPurchase} instance from a {@link ProofOfPurchaseDto} and a {@link User}.
     *
     * @param dto  the {@link ProofOfPurchaseDto} containing the proof of purchase data
     * @param user the {@link User} to associate with the proof of purchase
     * @return a new {@link ProofOfPurchase} instance
     */
    private ProofOfPurchase createProofOfPurchaseWithDtoAndUser(ProofOfPurchaseDto dto, User user) {
        return ProofOfPurchase.builder()
                .shopName(dto.shopName())
                .reference(dto.reference())
                .buyDate(dto.buyDate())
                .warrantyEndDate(dto.warrantyEndDate())
                .description(dto.description())
                .user(user)
                .products(List.of(createProduct(dto)))
                .build();
    }

    /**
     * Helper method to create a {@link ProofOfPurchase} instance associated with a {@link User}.
     *
     * @param user the {@link User} to associate with the proof of purchase
     * @return a new {@link ProofOfPurchase} instance
     */
    private ProofOfPurchase createProofOfPurchaseWithUser(User user) {
        ProofOfPurchase proofOfPurchase = new ProofOfPurchase();
        proofOfPurchase.setUser(user);
        return proofOfPurchase;
    }

    /**
     * Helper method to create a {@link Product} instance from a {@link ProofOfPurchaseDto}.
     *
     * @param dto the {@link ProofOfPurchaseDto} containing the product data
     * @return a new {@link Product} instance
     */
    private Product createProduct(ProofOfPurchaseDto dto) {
        ProductDto productDto = dto.products().getFirst();
        return Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .build();
    }

    /**
     * Helper method to verify that a {@link ProofOfPurchase} was saved with the correct data.
     *
     * @param dto  the {@link ProofOfPurchaseDto} containing the expected data
     * @param user the {@link User} associated with the proof of purchase
     */
    private void verifyProofOfPurchaseSaved(ProofOfPurchaseDto dto, User user) {
        verify(proofOfPurchaseRepository).save(proofOfPurchaseArgumentCaptor.capture());
        ProofOfPurchase savedProof = proofOfPurchaseArgumentCaptor.getValue();
        assertThat(savedProof.getShopName()).isEqualTo(dto.shopName());
        assertThat(savedProof.getReference()).isEqualTo(dto.reference());
        assertThat(savedProof.getUser()).isEqualTo(user);
    }

    /**
     * Helper method to verify that a {@link ProofOfPurchase} was updated with the correct data.
     *
     * @param dto the {@link ProofOfPurchaseDto} containing the expected updated data
     */
    private void verifyProofOfPurchaseUpdated(ProofOfPurchaseDto dto) {
        verify(proofOfPurchaseRepository).save(proofOfPurchaseArgumentCaptor.capture());
        ProofOfPurchase updatedProof = proofOfPurchaseArgumentCaptor.getValue();
        assertThat(updatedProof.getShopName()).isEqualTo(dto.shopName());
    }
}
