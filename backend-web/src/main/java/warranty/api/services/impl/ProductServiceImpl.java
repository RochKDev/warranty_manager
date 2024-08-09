package warranty.api.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import warranty.api.exception.ProductNotFoundException;
import warranty.api.exception.ProofOfPurchaseNotFoundException;
import warranty.api.model.Product;
import warranty.api.model.ProofOfPurchase;
import warranty.api.model.dto.ProductDto;
import warranty.api.model.responses.ProductResponseDto;
import warranty.api.repository.ProductRepository;
import warranty.api.repository.ProofOfPurchaseRepository;
import warranty.api.services.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProofOfPurchaseRepository proofOfPurchaseRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProofOfPurchaseRepository proofOfPurchaseRepository) {
        this.productRepository = productRepository;
        this.proofOfPurchaseRepository = proofOfPurchaseRepository;
    }

    @Override
    public ProductResponseDto save(ProductDto productDto) {
        // Fetch the ProofOfPurchase by its ID
        Optional<ProofOfPurchase> proofOfPurchaseOpt = proofOfPurchaseRepository.findById(productDto.proofOfPurchaseId());

        if (proofOfPurchaseOpt.isEmpty()) {
            throw new ProofOfPurchaseNotFoundException("Proof of purchase with id " + productDto.proofOfPurchaseId() + " not found");
        }

        ProofOfPurchase proofOfPurchase = proofOfPurchaseOpt.get();

        // Convert the ProductDto to a Product entity
        Product product = Product.builder()
                .name(productDto.name())
                .description(productDto.description())
                .proofOfPurchase(proofOfPurchase)
                .build();

        log.info("Saving product with name {}", product.getName());

        // Save the product and convert the saved entity to a ProductResponseDto
        Product savedProduct = productRepository.save(product);

        return ProductResponseDto.fromEntity(savedProduct);
    }


    @Override
    public List<ProductResponseDto> findAll() {
        log.info("Finding all products");
        List<Product> products = productRepository.findAll();

        // Convert the list of Product entities to a list of ProductResponseDto
        return products.stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDto> findByShopNameAndReference(String shopName, String reference) {
        log.info("Finding products with shop name {} and reference {}", shopName, reference);
        List<Product> products = productRepository.findByProofOfPurchase_ShopNameAndProofOfPurchase_Reference(shopName, reference);

        // Convert the list of Product entities to a list of ProductResponseDto
        return products.stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto findOneById(Long id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }

        log.info("Finding product with id {}", id);
        return ProductResponseDto.fromEntity(product.get());
    }

    @Override
    public ProductResponseDto update(Long id, ProductDto productDto) {
        // Find the ProofOfPurchase by its ID
        Optional<ProofOfPurchase> proofOfPurchaseOpt = proofOfPurchaseRepository.findById(productDto.proofOfPurchaseId());

        if (proofOfPurchaseOpt.isEmpty()) {
            throw new ProofOfPurchaseNotFoundException("Proof of purchase with id " + productDto.proofOfPurchaseId() + " not found");
        }

        ProofOfPurchase proofOfPurchase = proofOfPurchaseOpt.get();

        // Find the Product by its ID
        Optional<Product> existingProductOpt = productRepository.findById(id);

        if (existingProductOpt.isEmpty()) {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }

        Product existingProduct = existingProductOpt.get();

        // Update the Product entity with data from the DTO
        existingProduct.setName(productDto.name());
        existingProduct.setDescription(productDto.description());
        existingProduct.setProofOfPurchase(proofOfPurchase);

        log.info("Updating product with id {}", id);

        // Save the updated Product entity and convert it to a ProductResponseDto
        Product updatedProduct = productRepository.save(existingProduct);

        return ProductResponseDto.fromEntity(updatedProduct);
    }

    @Override
    public void deleteById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }

        log.info("Deleting product with id {}", id);
        productRepository.deleteById(id);
    }
}
