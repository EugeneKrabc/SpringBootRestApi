package com.edu.ulab.app.dao;

import com.edu.ulab.app.dto.BookDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long saveBook(BookDto bookDto) {
        final String INSERT_SQL = "INSERT INTO BOOK(TITLE, AUTHOR, PAGE_COUNT, USER_ID) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                        ps.setString(1, bookDto.getTitle());
                        ps.setString(2, bookDto.getAuthor());
                        ps.setLong(3, bookDto.getPageCount());
                        ps.setLong(4, bookDto.getPersonId());
                        return ps;
                    }
                },
                keyHolder);
        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        log.info("Save user into bd, id = {}", id);
        return id;
    }

    public List<Long> getBooksWithThisUserId(Long userId) {
        final String SELECT_SQL = "SELECT ID FROM BOOK WHERE USER_ID = " + userId;
        return jdbcTemplate.queryForList(SELECT_SQL, Long.class);
    }

    public void deleteBooksWithThisUserId(Long userId) {
        int amount = jdbcTemplate.update("DELETE FROM BOOK WHERE USER_ID = ?", userId);
        log.info("Number of deleted books from bd: {}", amount);
    }

    public void updateBook(BookDto bookDto) {
        int amount = jdbcTemplate.update(
                "UPDATE BOOK SET TITLE = ?, AUTHOR = ?, PAGE_COUNT = ?, USER_ID = ?, ID = ?",
                bookDto.getTitle(), bookDto.getAuthor(), bookDto.getPageCount(),
                bookDto.getPersonId(), bookDto.getId()
        );
        log.info("Update query return: {}", amount);
    }

    public BookDto getBookByBookId(Long id) {
        final String SELECT_SQL = "SELECT FROM BOOK WHERE ID = " + id;
        return jdbcTemplate.query(SELECT_SQL, new BeanPropertyRowMapper<>(BookDto.class)).stream()
                .findAny().orElse(null);
    }

    public void deleteBookById(Long id) {
        int amount = jdbcTemplate.update("DELETE FROM BOOK WHERE ID = ?", id);
        log.info("Update query return: {}", amount);
    }
}
