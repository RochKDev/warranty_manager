package warranty.api.services;

import warranty.api.model.Product;
import warranty.api.model.dto.ProductDto;
import warranty.api.model.responses.ProductResponseDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductResponseDto save(final ProductDto productDto);

    List<ProductResponseDto> findAll();

    List<ProductResponseDto> findByShopNameAndReference(final String shopName, final String reference);

//    List<Product> findByUserId(final Long userId);

    ProductResponseDto findOneById(final Long id);

    ProductResponseDto update(final Long id, ProductDto productDto);

    void deleteById(final Long id);
}
