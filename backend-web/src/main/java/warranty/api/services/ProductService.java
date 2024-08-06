package warranty.api.services;

import warranty.api.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product save(Product product);

    List<Product> findByShopNameAndReference(String shopName, String reference);

    List<Product> findByUserId(Long userId);

    Optional<Product> findOne(Long id);

    Product update(Long id, Product product);

    void deleteById(Long id);
}
