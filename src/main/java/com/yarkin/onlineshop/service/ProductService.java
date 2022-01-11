package com.yarkin.onlineshop.service;

import com.yarkin.onlineshop.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAll();

    Product get(int id);

    // Returns inserted product
    Product add(Product product);

    void update(int id, Product product);

    void delete(int id);
}
