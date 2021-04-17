package com.compasso.projectms.api.resource;


import com.compasso.projectms.domain.entity.Product;
import com.compasso.projectms.domain.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import static com.compasso.projectms.domain.repository.spec.ProductSpec.productMinMax;
import static com.compasso.projectms.domain.repository.spec.ProductSpec.productWithNameOrDescription;
import static org.springframework.data.jpa.domain.Specification.where;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {

    private final ProductService productService;

    @Autowired
    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> insert(@Valid @RequestBody Product product) {
        product = productService.insert(product);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();
        return ResponseEntity.created(uri).body(product);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> update(@PathVariable String id, @Valid @RequestBody Product product) {
        product = productService.update(id, product);
        return ResponseEntity.ok().body(product);
    }

    @GetMapping(value ="/{id}")
    public Product findById(@PathVariable String id) {
        return productService.consult(id);
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        List<Product> list = productService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<Product>> filter(
            @RequestParam(value = "q", required = false) String nameOrDescription,
            @RequestParam(value = "min_price", required = false) BigDecimal minPrice,
            @RequestParam(value = "max_price", required = false) BigDecimal maxPrice) {

        final List<Product> products = this.productService.findAllBySpecification(
                where(productWithNameOrDescription(nameOrDescription)).
                and(productMinMax(minPrice, maxPrice)));

        return ResponseEntity.ok(products);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Product> delete(@PathVariable String id) {
        productService.delete(id);
        return ResponseEntity.ok().build();
    }
}
