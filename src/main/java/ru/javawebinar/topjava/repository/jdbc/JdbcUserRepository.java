package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ConstraintViolationValidator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final ConstraintViolationValidator constraintViolationValidator;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                              ConstraintViolationValidator constraintViolationValidator) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.constraintViolationValidator = constraintViolationValidator;
    }

    @Override
    @Transactional
    public User save(User user) {
        constraintViolationValidator.validate(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {
                return null;
            }
            jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", user.getId());
        }
        jdbcTemplate.batchUpdate("INSERT INTO user_role(user_id, role) VALUES(?, ?)",
                user.getRoles(),
                user.getRoles().size(),
                new ParameterizedPreparedStatementSetter<Role>() {
                    @Override
                    public void setValues(PreparedStatement ps, Role argument) throws SQLException {
                        ps.setInt(1, user.getId());
                        ps.setString(2, argument.name());
                    }
                }
        );
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", id);
        CustomUsersWithRolesRowCallbackHandler customUsersWithRolesRowCallbackHandler = new CustomUsersWithRolesRowCallbackHandler();
        namedParameterJdbcTemplate.query("SELECT * FROM users LEFT JOIN user_role ur ON users.id = ur.user_id WHERE id=:id",
                mapSqlParameterSource, customUsersWithRolesRowCallbackHandler);
        return DataAccessUtils.singleResult(customUsersWithRolesRowCallbackHandler.getUsersWithRoles());
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("email", email);
        CustomUsersWithRolesRowCallbackHandler customUsersWithRolesRowCallbackHandler = new CustomUsersWithRolesRowCallbackHandler();
        namedParameterJdbcTemplate.query("SELECT * FROM users LEFT JOIN user_role ur ON users.id = ur.user_id WHERE email=:email",
                mapSqlParameterSource, customUsersWithRolesRowCallbackHandler);
        return DataAccessUtils.singleResult(customUsersWithRolesRowCallbackHandler.getUsersWithRoles());
    }

    @Override
    public List<User> getAll() {
        CustomUsersWithRolesRowCallbackHandler customUsersWithRolesRowCallbackHandler = new CustomUsersWithRolesRowCallbackHandler();
        jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_role ur ON users.id = ur.user_id",
                customUsersWithRolesRowCallbackHandler);
        return customUsersWithRolesRowCallbackHandler.getUsersWithRoles();
    }

    private static class CustomUsersWithRolesRowCallbackHandler implements RowCallbackHandler {

        private final Map<Integer, User> users = new HashMap<>();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            do {
                User user = new User();
                Integer userId = rs.getInt("id");
                if (!users.containsKey(userId)) {
                    user.setId(userId);
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPassword(rs.getString("password"));
                    user.setRegistered(rs.getDate("registered"));
                    user.setEnabled(rs.getBoolean("enabled"));
                    user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                }
                String role = rs.getString("role");
                user.setRoles(new HashSet<>());
                if (Objects.nonNull(role)) {
                    user.getRoles().add(Role.valueOf(role));
                }
                users.merge(userId, user, (oldUser, newUser) -> {
                    oldUser.getRoles().addAll(newUser.getRoles());
                    return oldUser;
                });
            } while (rs.next());
        }

        public List<User> getUsersWithRoles() {
            return users
                    .values()
                    .stream()
                    .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                    .collect(Collectors.toList());
        }
    }
}
