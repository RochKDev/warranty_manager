package warranty.api.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import warranty.api.exception.ProductNotFoundException;
import warranty.api.model.Product;
import warranty.api.repository.ProductRepository;
import warranty.api.services.ProductService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        log.info("Saving product with id {}", product.getId());
        return productRepository.save(product);
    }

    @Override
    public List<Product> findByShopNameAndReference(String shopName, String reference) {
        log.info("Finding product with shop name {} and reference {}", shopName, reference);
        return productRepository.findByProofOfPurchase_ShopNameAndProofOfPurchase_Reference(shopName, reference);
    }

    @Override
    public List<Product> findByUserId(Long userId) {
        log.info("Finding all products for user with id {}", userId);
        return productRepository.findByProofOfPurchase_User_Id(userId);
    }

    @Override
    public Optional<Product> findOne(Long id) {
        log.info("Finding product with id {}", id);
        return productRepository.findById(id);
    }

    @Override
    public Product update(Long id, Product product) {
        product.setId(id);

        // If the product does not exist return an error
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }
        log.info("Updating product with id {}", id);
        return productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        log.info("Deleting product with id {}", id);
        productRepository.deleteById(id);
    }
}
