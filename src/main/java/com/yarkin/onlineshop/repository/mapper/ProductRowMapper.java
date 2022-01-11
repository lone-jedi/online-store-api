package com.yarkin.onlineshop.repository.mapper;

import com.yarkin.onlineshop.entity.Product;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        double price = resultSet.getDouble("price");
        Timestamp creationDate = resultSet.getTimestamp("creation_date");

        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .creationDate(creationDate.toLocalDateTime())
                .build();
    }

    public Product mapRow(Map<String, Object> keys) {
        return Product.builder()
                .id((int) keys.get("id"))
                .name(keys.get("name").toString())
                .price(((BigDecimal) keys.get("price")).doubleValue())
                .creationDate(((Timestamp) keys.get("creation_date")).toLocalDateTime())
                .build();
    }
}
