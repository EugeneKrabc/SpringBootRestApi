package com.edu.ulab.app.dao;

import com.edu.ulab.app.dto.PersonDto;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLType;
import java.util.Objects;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long savePerson(PersonDto userDto) {
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

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void deleteUser(Long id) {
        jdbcTemplate.update("DELETE FROM PERSON WHERE ID = ?", id);
    }

    public PersonDto getUserById(Long id) {
        final String SELECT_SQL = "SELECT FROM PERSON WHERE ID = " + id;
        return jdbcTemplate.query(SELECT_SQL, new BeanPropertyRowMapper<>(PersonDto.class)).stream()
                .findAny().orElse(null);
    }

    public void updateUser(PersonDto userDto) {
        jdbcTemplate.update("UPDATE PERSON SET FULL_NAME = ?, TITLE = ?, AGE = ? WHERE ID = ?",
                userDto.getFullName(), userDto.getTitle(), userDto.getAge(), userDto.getId());
    }
}
