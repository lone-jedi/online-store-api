package com.yarkin.onlineshop.repository.jdbc;

import com.yarkin.onlineshop.entity.Product;
import com.yarkin.onlineshop.repository.ProductRepository;
import com.yarkin.onlineshop.repository.mapper.ProductRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class JdbcProductRepository implements ProductRepository {
    private static final String SELECT_ALL_SQL = "SELECT id, name, price, creation_date FROM product;";
    private static final String SELECT_BY_ID_SQL = "SELECT id, name, price FROM product WHERE id=:id;";
    private static final String INSERT_SQL = """
            INSERT INTO product(name, price) VALUES 
            (:name, :price);""";
    private static final String UPDATE_SQL = """
            UPDATE product SET name=:name, price=:price WHERE id=:id""";
    private static final String DELETE_SQL = "DELETE FROM product WHERE id=:id";

    private static final ProductRowMapper PRODUCT_ROW_MAPPER = new ProductRowMapper();

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Product> getAll() {
        return jdbcTemplate.query(SELECT_ALL_SQL, PRODUCT_ROW_MAPPER);
    }

    @Override
    public Product get(int id) {
        return jdbcTemplate.queryForObject(SELECT_BY_ID_SQL, Map.of("id", id), PRODUCT_ROW_MAPPER);
    }

    @Override
    public Product add(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
        sqlParameterSource.addValue("name", product.getName());
        sqlParameterSource.addValue("price", product.getPrice());

        jdbcTemplate.update(INSERT_SQL, sqlParameterSource, keyHolder);

        return PRODUCT_ROW_MAPPER.mapRow(keyHolder.getKeys());
    }

    @Override
    public void update(int id, Product product) {
        jdbcTemplate.update(UPDATE_SQL, Map.of("id", id));
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_SQL, Map.of("id", id));
    }
}
