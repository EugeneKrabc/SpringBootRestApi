package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BookServiceImpl implements BookService {
    Storage storage;

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        bookDto.setId(storage.saveBook(bookDto));
        return bookDto;
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        return null;
    }

    @Override
    public BookDto getBookById(Long id) {
        return null;
    }

    @Override
    public List<Long> getBookIdListByUserId(Long userId) {
        return storage.getBookIdListByUserId(userId);
    }

    @Override
    public void deleteBookById(Long id) {

    }
}
