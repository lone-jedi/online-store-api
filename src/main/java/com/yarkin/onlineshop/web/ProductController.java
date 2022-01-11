package com.yarkin.onlineshop.web;

import com.yarkin.onlineshop.entity.Product;
import com.yarkin.onlineshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<Product> findAll() {
        List<Product> products = productService.getAll();
        log.info("Obtained: {} products", products.size());
        log.debug("All products: {}", products);
        return products;
    }

    @GetMapping("/{id}")
    public Object get(@PathVariable int id) {
        try {
            Product product = productService.get(id);
            log.info("Get product by id:{}\n {}", id, product);
            return product;
        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Product product) {
        try {
            if(product.getName() == null) {
                throw new RuntimeException("Error! Missing name parameter");
            }

            log.info("Add product: {}", product);
            Product insertedProduct = productService.add(product);
            return new ResponseEntity<>(
                    Map.of("product", insertedProduct, "message", "Product " + product.getName() + " successfully added"),
                    HttpStatus.CREATED);
        } catch(RuntimeException e) {
            return new ResponseEntity<>(
                    Map.of("message", e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }

    }
}
