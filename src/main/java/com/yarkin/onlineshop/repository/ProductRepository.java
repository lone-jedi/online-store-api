package com.yarkin.onlineshop.repository;

import com.yarkin.onlineshop.entity.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> getAll();

    Product get(int id);

    // Returns inserted product info
    Product add(Product product);

    void update(int id, Product product);

    void delete(int id);
}
