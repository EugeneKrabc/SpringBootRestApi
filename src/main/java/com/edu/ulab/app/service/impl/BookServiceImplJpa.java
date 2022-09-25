package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BookServiceImplJpa implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public BookServiceImplJpa(BookRepository bookRepository,
                              BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = bookMapper.bookDtoToBook(bookDto);
        log.info("Mapped book: {}", book);
        Book savedBook = bookRepository.save(book);
        log.info("Saved book: {}", savedBook);
        return bookMapper.bookToBookDto(savedBook);
    }

    @Override
    public void updateBook(BookDto bookDto) {
        bookRepository.save(bookMapper.bookDtoToBook(bookDto));
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookMapper.bookToBookDto(bookRepository.findById(id).orElse(null));
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Long> getBookIdListForUser(Long userId) {
        return bookRepository.getBookIdListWithUserId(userId).stream().toList();
    }

    public void deleteBooksWithUserId(Long userId) {
        bookRepository.deleteByUserId(userId);
    }
}
