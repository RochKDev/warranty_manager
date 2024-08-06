package warranty.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import warranty.api.model.Product;
import warranty.api.model.dto.ProductDto;
import warranty.api.services.ProductService;

@RestController
@RequestMapping(path = "api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.save(productDto), HttpStatus.CREATED);
    }


}
