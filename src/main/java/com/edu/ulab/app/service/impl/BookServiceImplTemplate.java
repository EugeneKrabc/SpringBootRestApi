package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dao.BookDAO;
import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BookServiceImplTemplate implements BookService {

    private final BookDAO bookDAO;

    public BookServiceImplTemplate(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        bookDto.setId(bookDAO.saveBook(bookDto));
        return bookDto;
    }

    @Override
    public void updateBook(BookDto bookDto) {
        bookDAO.updateBook(bookDto);
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookDAO.getBookByBookId(id);
    }

    @Override
    public void deleteBookById(Long id) {
        bookDAO.deleteBookById(id);
    }

    public List<Long> getBookIdListForUser(Long personId) {
        return bookDAO.getBooksWithThisUserId(personId);
    }

    public void deleteBooksWithUserId(Long userId) {
        bookDAO.deleteBooksWithThisUserId(userId);
    }
}
