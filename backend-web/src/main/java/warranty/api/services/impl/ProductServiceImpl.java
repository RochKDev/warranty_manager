package warranty.api.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
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
import warranty.api.services.ProductService;

import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProofOfPurchaseRepository proofOfPurchaseRepository;
    private final UserRepository userRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              ProofOfPurchaseRepository proofOfPurchaseRepository,
                              UserRepository userRepository) {
        this.productRepository = productRepository;
        this.proofOfPurchaseRepository = proofOfPurchaseRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProductResponseDto save(ProductDto productDto, UserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);
        ProofOfPurchase proofOfPurchase = getValidProofOfPurchase(productDto.getProofOfPurchaseId(), user);

        Product product = buildProductFromDto(productDto, proofOfPurchase);

        log.debug("Saving product with name {}", product.getName());

        Product savedProduct = productRepository.save(product);

        return ProductResponseDto.fromEntity(savedProduct);
    }

    @Override
    public Page<ProductResponseDto> findAll(UserDetails userDetails, Pageable pageable) {
        User user = getUserFromUserDetails(userDetails);

        log.debug("Finding all products related to user: {}", user.getEmail());

        return productRepository.findByProofOfPurchase_User_Id(user.getId(), pageable)
                .map(ProductResponseDto::fromEntity);
    }

    @Override
    public ProductResponseDto findOneById(Long id, UserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);
        Product product = getAuthorizedProduct(id, user);

        log.debug("Finding product with id {}", id);

        return ProductResponseDto.fromEntity(product);
    }

    @Override
    public ProductResponseDto update(Long id, ProductDto productDto, UserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);
        ProofOfPurchase proofOfPurchase = getValidProofOfPurchase(productDto.getProofOfPurchaseId(), user);
        Product existingProduct = getAuthorizedProduct(id, user);

        updateProductFromDto(existingProduct, productDto, proofOfPurchase);

        log.debug("Updating product with id {}", id);

        Product updatedProduct = productRepository.save(existingProduct);

        return ProductResponseDto.fromEntity(updatedProduct);
    }

    @Override
    public void deleteById(Long id, UserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails);
        Product product = getAuthorizedProduct(id, user);

        log.debug("Deleting product with id {}", id);

        productRepository.deleteById(id);
    }

    // Helper method to get user from UserDetails
    private User getUserFromUserDetails(UserDetails userDetails) {
        Optional<User> userOpt = userRepository.findByEmail(userDetails.getUsername());
        if (userOpt.isEmpty()) {
            throw new UserEmailNotFoundException("User with email " + userDetails.getUsername() + " not found");
        }
        return userOpt.get();
    }

    // Helper method to get valid proof of purchase, it checks if the proof of purchase belongs to the user and it exists
    private ProofOfPurchase getValidProofOfPurchase(Long proofOfPurchaseId, User user) {
        Optional<ProofOfPurchase> proofOfPurchaseOpt = proofOfPurchaseRepository.findById(proofOfPurchaseId);
        if (proofOfPurchaseOpt.isEmpty()) {
            throw new ProofOfPurchaseNotFoundException("Proof of purchase with id " + proofOfPurchaseId + " not found");
        }
        ProofOfPurchase proofOfPurchase = proofOfPurchaseOpt.get();
        if (!proofOfPurchase.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedResourceAccess("You are not authorized to access this proof of purchase.");
        }
        return proofOfPurchase;
    }

    // Helper method to get product by id and check if the user is authorized to access it
    private Product getAuthorizedProduct(Long productId, User user) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            throw new ProductNotFoundException("Product with id " + productId + " not found");
        }
        Product product = productOpt.get();
        if (!product.getProofOfPurchase().getUser().getId().equals(user.getId())) {
            throw new UnauthorizedResourceAccess("You are not authorized to access this product.");
        }
        return product;
    }

    // Helper method to build product from DTO
    private Product buildProductFromDto(ProductDto productDto, ProofOfPurchase proofOfPurchase) {
        return Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .proofOfPurchase(proofOfPurchase)
                .build();
    }

    // Helper method to update product from DTO
    private void updateProductFromDto(Product product, ProductDto productDto, ProofOfPurchase proofOfPurchase) {
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setProofOfPurchase(proofOfPurchase);
    }
}
