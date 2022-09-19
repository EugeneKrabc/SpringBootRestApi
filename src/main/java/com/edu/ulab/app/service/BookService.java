package com.edu.ulab.app.service;


import com.edu.ulab.app.dto.BookDto;

import java.util.List;

public interface BookService {
    BookDto createBook(BookDto userDto);

    BookDto getBookById(Long id);

    List<Long> getBookIdListByUserId(Long userId);

    void deleteBookById(Long id);
}
