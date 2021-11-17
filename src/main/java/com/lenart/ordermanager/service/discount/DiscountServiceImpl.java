package com.lenart.ordermanager.service.discount;

import com.lenart.ordermanager.service.discount.pojo.Discount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class DiscountServiceImpl implements DiscountService {

    private JdbcTemplate jdbcTemplate;

    private RowMapper<Discount> rowMapper = (rs, rowNum) -> {
        Discount discount = new Discount();
        discount.setCode((rs.getString("discount_code")));
        discount.setValue((rs.getInt("discount_value")));
        return discount;
    };

    public DiscountServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Discount> findByCode(String code) {
        String sql = "select discount_code, discount_value from manager.discounts where discount_code = ?";
        return jdbcTemplate.query(sql, rowMapper, code).stream().findFirst();
    }

    @Override
    public Optional<String> findCodeByCode(String code) {
        String discountCode = "";
        RowMapper<String> rM = (rs, rowNum) -> rs.getString("discount_code");

        String sql = "select discount_code from manager.discounts where discount_code = ?";
        return jdbcTemplate.query(sql, rM, code).stream().findFirst();
    }

    @Override
    public void save(String code, int value) {
        String sql = "insert into manager.discounts (discount_code, discount_value) values (?, ?)";
        jdbcTemplate.update(sql, code, value);
    }

    @Override
    public int discountValue(String code) {
        return code.replaceAll("\\D", "").length();
    }

    @Override
    public boolean isCodeUsed(String code) {
        return !(findCodeByCode(code).isEmpty() && isValidCode(code));
    }

    @Override
    public boolean isValidCode(String code) {

        return code.length() >= 2 && code.length() <= 8;
    }
}
