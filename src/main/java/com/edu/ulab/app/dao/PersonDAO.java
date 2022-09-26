package com.edu.ulab.app.dao;

import com.edu.ulab.app.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.Objects;

@Slf4j
@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long savePerson(UserDto userDto) {
        final String INSERT_SQL = "INSERT INTO PERSON(FULL_NAME, TITLE, AGE) VALUES (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                    ps.setString(1, userDto.getFullName());
                    ps.setString(2, userDto.getTitle());
                    ps.setLong(3, userDto.getAge());
                    return ps;
                }, keyHolder);
        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        log.info("Save user into bd, id = {}", id);
        return id;
    }

    public void deleteUser(Long id) {
        int amount = jdbcTemplate.update("DELETE FROM PERSON WHERE ID = ?", id);
        log.info("Number of deleted books from bd: {}", amount);
    }

    public UserDto getUserById(Long id) {
        final String SELECT_SQL = "SELECT FROM PERSON WHERE ID = " + id;
        return jdbcTemplate.query(SELECT_SQL, new BeanPropertyRowMapper<>(UserDto.class)).stream()
                .findAny().orElse(null);
    }

    public void updateUser(UserDto userDto) {
        int amount = jdbcTemplate.update("UPDATE PERSON SET FULL_NAME = ?, TITLE = ?, AGE = ? WHERE ID = ?",
                userDto.getFullName(), userDto.getTitle(), userDto.getAge(), userDto.getId());
        log.info("Update query return: {}", amount);
    }
}
