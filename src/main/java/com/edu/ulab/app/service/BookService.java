package com.edu.ulab.app.service;


import com.edu.ulab.app.dto.BookDto;

import java.util.List;

public interface BookService {
    BookDto createBook(BookDto userDto);

    List<Long> getBookIdListByUserId(Long userId);

    BookDto getBookById(Long bookId);

    void deleteBookById(Long bookId);
}
