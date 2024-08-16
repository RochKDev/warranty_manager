package warranty.api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import warranty.api.model.dto.ProductDto;
import warranty.api.model.responses.ProductResponseDto;

import java.util.List;

public interface ProductService {

    ProductResponseDto save(final ProductDto productDto, UserDetails userDetails);

    Page<ProductResponseDto> findAll(UserDetails userDetails, Pageable pageable);

    List<ProductResponseDto> findByShopNameAndReference(final String shopName, final String reference);

    ProductResponseDto findOneById(final Long id, UserDetails userDetails);

    ProductResponseDto update(final Long id, ProductDto productDto, UserDetails userDetails);

    void deleteById(final Long id, UserDetails userDetails);
}
