package woowacourse.shoppingcart.dao;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import woowacourse.shoppingcart.domain.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CustomerDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<Customer> customerRowMapper = (rs, rowNum) ->
            new Customer(
                    rs.getLong("id"),
                    rs.getString("account"),
                    rs.getString("nickname"),
                    rs.getString("password"),
                    rs.getString("address"),
                    rs.getString("phone_number")
            );

    public CustomerDao(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    public Customer save(final Customer customer) {
        final SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("customer")
                .usingGeneratedKeyColumns("id");

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("account", customer.getAccount());
        parameters.put("nickname", customer.getNickname());
        parameters.put("password", customer.getPassword());
        parameters.put("address", customer.getAddress());
        parameters.put("phone_number", customer.getPhoneNumber());

        final Number number = simpleJdbcInsert.executeAndReturnKey(parameters);
        return new Customer(number.longValue(), customer.getAccount(), customer.getNickname(), customer.getPassword(), customer.getAddress(), customer.getPhoneNumber());
    }

    public Optional<Customer> findByAccount(String account) {
        final String sql = "SELECT id, account, nickname, password, address, phone_number " +
                "FROM CUSTOMER WHERE account=:account";

        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("account", account);

        final List<Customer> query = namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource(parameters), customerRowMapper);
        return Optional.ofNullable(DataAccessUtils.singleResult(query));
    }

    public Long findIdByUserName(String customerName) {
        return null;
    }

    public Optional<Customer> findById(long customerId) {
        final String sql = "SELECT id, account, nickname, password, address, phone_number " +
                "FROM customer WHERE id=:customerId";

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("customerId", customerId);

        final List<Customer> result = namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource(parameters), customerRowMapper);
        return Optional.ofNullable(DataAccessUtils.singleResult(result));
    }
}
