package warranty.api.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import warranty.api.exception.ProductNotFoundException;
import warranty.api.exception.ProofOfPurchaseNotFoundException;
import warranty.api.model.Product;
import warranty.api.model.ProofOfPurchase;
import warranty.api.model.dto.ProductDto;
import warranty.api.repository.ProductRepository;
import warranty.api.repository.ProofOfPurchaseRepository;
import warranty.api.services.ProductService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProofOfPurchaseRepository proofOfPurchaseRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProofOfPurchaseRepository proofOfPurchaseRepository){
        this.productRepository = productRepository;
        this.proofOfPurchaseRepository = proofOfPurchaseRepository;
    }

    @Override
    public Product save(ProductDto productDto) {
        // Fetch the ProofOfPurchase by its composite key
        ProofOfPurchase proofOfPurchase = proofOfPurchaseRepository.findById(productDto.proofOfPurchaseId())
                .orElseThrow(() -> new ProofOfPurchaseNotFoundException("Proof of purchase with id "
                        + productDto.proofOfPurchaseId() + " not found"));

        // Convert the ProductDto to a Product
        Product product = Product.builder()
                .name(productDto.name())
                .description(productDto.description())
                .proofOfPurchase(proofOfPurchase)
                .build();

        log.info("Saving product with name {}", product.getName());
        return productRepository.save(product);
    }

    @Override
    public List<Product> findAll() {
        log.info("Finding all products");
        return productRepository.findAll();
    }

    @Override
    public List<Product> findByShopNameAndReference(String shopName, String reference) {
        log.info("Finding product with shop name {} and reference {}", shopName, reference);
        return productRepository.findByProofOfPurchase_ShopNameAndProofOfPurchase_Reference(shopName, reference);
    }

//    @Override
//    public List<Product> findByUserId(Long userId) {
//        log.info("Finding all products for user with id {}", userId);
//        return productRepository.findByProofOfPurchase_User_Id(userId);
//    }

    @Override
    public Product findOne(Long id) {
        Optional<Product> product = productRepository.findById(id);
        // If the product does not exist return an error
        if (product.isEmpty()) {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }

        log.info("Finding product with id {}", id);
        return product.get();
    }

    @Override
    public Product update(Long id, ProductDto productDto) {
        // Fetch the ProofOfPurchase by its composite key
        ProofOfPurchase proofOfPurchase = proofOfPurchaseRepository.findById(productDto.proofOfPurchaseId())
                .orElseThrow(() -> new ProofOfPurchaseNotFoundException("Proof of purchase with id "
                        + productDto.proofOfPurchaseId() + " not found"));

        // Convert the ProductDto to a Product
        Product product = Product.builder()
                .name(productDto.name())
                .description(productDto.description())
                .proofOfPurchase(proofOfPurchase)
                .build();

        log.info("Saving product with name {}", product.getName());
        return productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {

        // If the product does not exist return an error
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }

        log.info("Deleting product with id {}", id);
        productRepository.deleteById(id);
    }
}
