package com.yarkin.onlineshop.service.product;

import com.yarkin.onlineshop.entity.Product;
import com.yarkin.onlineshop.repository.ProductRepository;
import com.yarkin.onlineshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<Product> getAll() {
        return productRepository.getAll();
    }

    @Override
    public Product get(int id) {
        return productRepository.get(id);
    }

    @Override
    public Product add(Product product) {
        return productRepository.add(product);
    }

    @Override
    public void update(int id, Product product) {

    }

    @Override
    public void delete(int id) {

    }
}
