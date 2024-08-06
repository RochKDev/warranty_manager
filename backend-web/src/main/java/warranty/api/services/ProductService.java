package warranty.api.services;

import warranty.api.model.Product;
import warranty.api.model.dto.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product save(final ProductDto productDto);

    List<Product> findByShopNameAndReference(final String shopName, final String reference);

    List<Product> findByUserId(final Long userId);

    Optional<Product> findOne(final Long id);

    Product update(final Long id, Product product);

    void deleteById(final Long id);
}
