package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.exception.IncorrectRequestException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.web.request.BookRequest;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.response.UserBookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class UserDataFacade {
    private final UserService userService;
    private final BookService bookService;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public UserDataFacade(UserService userService,
                          BookService bookService,
                          UserMapper userMapper,
                          BookMapper bookMapper) {
        this.userService = userService;
        this.bookService = bookService;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    public UserBookResponse createUserWithBooks(UserBookRequest userBookRequest) {
        checkCorrectRequest(userBookRequest);
        log.info("Got user book create request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        UserDto createdUser = userService.createUser(userDto);
        log.info("Created user: {}", createdUser);

        List<Long> bookIdList = saveBooksToUser(userBookRequest.getBookRequests(), createdUser.getId());

        return UserBookResponse.builder()
                .userId(createdUser.getId())
                .booksIdList(bookIdList)
                .build();
    }

    public UserBookResponse updateUserWithBooks(UserBookRequest userBookRequest, Long userId) {
        log.info("Get user book update request: {}", userBookRequest);

        checkCorrectRequest(userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        UserDto updatedUser = userService.updateUser(userDto, userId);

        log.info("Updated user: {}", updatedUser);

        List<Long> bookIdList = saveBooksToUser(userBookRequest.getBookRequests(), userId);

        return UserBookResponse.builder()
                .userId(updatedUser.getId())
                .booksIdList(bookIdList)
                .build();
    }

    public UserBookResponse getUserWithBooks(Long userId) {
        log.info("Get user by id request: {}", userId);
        List<Long> booksIdList = bookService.getBookIdListByUserId(userId);
        return UserBookResponse.builder()
                .userId(userId)
                .booksIdList(booksIdList)
                .build();
    }

    public void deleteUserWithBooksById(Long userId) {
        log.info("Get user delete request: {}", userId);
        userService.deleteUserWithBooksById(userId);
    }

    private List<Long> saveBooksToUser(List<BookRequest> bookRequests, long userId) {
        if (bookRequests == null) {
            log.info("Request with empty book requests list, creating empty book ids");
            return new ArrayList<Long>();
        }

        List<Long> bookIdList = bookRequests
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(userId))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(BookDto::getId)
                .toList();
        log.info("Collected book ids: {}", bookIdList);
        return bookIdList;
    }

    private void checkCorrectRequest(UserBookRequest userBookRequest) {
        if (userBookRequest == null) {
            throw new IncorrectRequestException("UserBook request is null");
        }

        if (userBookRequest.getUserRequest() == null) {
            throw new IncorrectRequestException("User request is null");
        }
    }
}
