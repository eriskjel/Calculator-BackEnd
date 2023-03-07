package ntnu.dao;

import ntnu.models.User;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveUser(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword());
    }
}
