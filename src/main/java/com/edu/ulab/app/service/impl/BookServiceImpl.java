package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.exception.IncorrectDataException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.StorageDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BookServiceImpl implements BookService {
    StorageDAO storageDAO;

    BookMapper bookMapper;

    @Autowired
    public void setBookMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Autowired
    public void setStorageDAO(StorageDAO storageDAO) {
        this.storageDAO = storageDAO;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Long savedBookId = storageDAO.saveBook(bookMapper.bookDtoToBookEntity(bookDto));
        bookDto.setId(savedBookId);

        return bookDto;
    }

    @Override
    public List<Long> getBookIdListByUserId(Long userId) {
        return storageDAO.getBookIdListByUserId(userId);
    }

}
