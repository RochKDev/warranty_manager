package warranty.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import warranty.api.model.Product;
import warranty.api.model.dto.ProductDto;
import warranty.api.model.responses.ProductResponseDto;
import warranty.api.services.ProductService;

import java.util.List;

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
        return new ResponseEntity<>(productService.save(productDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProductResponseDto> getOne(@PathVariable Long id,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(productService.findOneById(id), HttpStatus.OK);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long id, @RequestBody ProductDto productDto,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(productService.update(id, productDto), HttpStatus.OK);
    }

    // TODO implement later partial updates

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
