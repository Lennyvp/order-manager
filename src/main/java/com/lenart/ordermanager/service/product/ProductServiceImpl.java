package com.lenart.ordermanager.service.product;

import com.lenart.ordermanager.service.product.pojo.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    private JdbcTemplate jdbcTemplate;

    private RowMapper<Product> rowMapper = (rs, rowNum) -> {
        Product product = new Product();
        product.setId((rs.getInt("id")));
        product.setName((rs.getString("name")));
        product.setDescription((rs.getString("description")));
        product.setImg((rs.getString("img")));
        product.setPrice((rs.getDouble("price")));
        return product;
    };

    public ProductServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("select * from manager.products", rowMapper);
    }

    @Override
    public Optional<Product> findById(int id) {
        String sql = "select * from manager.products where id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }


}
