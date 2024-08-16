package warranty.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import warranty.api.model.dto.ProductDto;
import warranty.api.model.responses.ProductResponseDto;
import warranty.api.services.ProductService;


@RestController
@RequestMapping(path = "api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(@RequestBody ProductDto productDto,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(productService.save(productDto, userDetails), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAll(@AuthenticationPrincipal UserDetails userDetails,
                                                           Pageable pageable) {
        return new ResponseEntity<>(productService.findAll(userDetails, pageable), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProductResponseDto> getOne(@PathVariable Long id,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(productService.findOneById(id, userDetails), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long id, @RequestBody ProductDto productDto,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(productService.update(id, productDto, userDetails), HttpStatus.OK);
    }

    // TODO implement later partial updates

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        productService.deleteById(id, userDetails);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
