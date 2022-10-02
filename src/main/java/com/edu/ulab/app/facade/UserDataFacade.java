package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.exception.IncorrectRequestException;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;

import com.edu.ulab.app.service.impl.BookServiceImplJpa;
import com.edu.ulab.app.service.impl.UserServiceImplJpa;
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
    private final UserServiceImplJpa userService;
    private final BookServiceImplJpa bookService;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public UserDataFacade(UserServiceImplJpa userService,
                          BookServiceImplJpa bookService,
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

        List<Long> bookIdList = saveBooksWithUserId(userBookRequest.getBookRequests(), createdUser.getId());

        return UserBookResponse.builder()
                .userId(createdUser.getId())
                .booksIdList(bookIdList)
                .build();
    }

    public UserBookResponse updateUserWithBooks(UserBookRequest userBookRequest, Long userId) {
        log.info("Got user book update request: {}", userBookRequest);
        checkCorrectRequest(userBookRequest);
        checkIfUserExist(userId);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        userDto.setId(userId);
        log.info("Mapped user request: {}", userDto);
        userService.updateUser(userDto);
        bookService.deleteBooksWithUserId(userId);
        List<Long> bookIdList = saveBooksWithUserId(userBookRequest.getBookRequests(), userId);
        return UserBookResponse.builder()
                .userId(userId)
                .booksIdList(bookIdList)
                .build();
    }

    public UserBookResponse getUserWithBooks(Long userId) {
        log.info("Receive Get user by id request: {}", userId);
        checkIfUserExist(userId);
        List<Long> bookIdList = bookService.getBookIdListForUser(userId);
        return UserBookResponse.builder()
                .userId(userId)
                .booksIdList(bookIdList)
                .build();
    }

    public void deleteUserWithBooks(Long userId) {
        log.info("Receive delete user by id request: {}", userId);
        checkIfUserExist(userId);
        userService.deleteUserById(userId);
        bookService.deleteBooksWithUserId(userId);
    }

    private void checkIfUserExist(Long userId) {
        if (userService.getUserById(userId) == null) {
            throw new NotFoundException("User with id = " + userId + " does not exist");
        }
        log.info("Checked that user with id = {} exists", userId);
    }

    private List<Long> saveBooksWithUserId(List<BookRequest> bookRequests, Long userId) {
        if (bookRequests == null) {
            log.info("Request with empty book requests list, creating empty book ids");
            return new ArrayList<Long>();
        }
        List<Long> savedBookIdList = bookRequests
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setPersonId(userId))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(BookDto::getId)
                .toList();
        log.info("Collected book ids: {}", savedBookIdList);
        return savedBookIdList;
    }

    private void checkCorrectRequest(UserBookRequest userBookRequest) {
        if (userBookRequest == null) {
            throw new IncorrectRequestException("UserBook request is null");
        }

        if (userBookRequest.getUserRequest() == null) {
            throw new IncorrectRequestException("User request is null");
        }
        log.info("UserBookRequest check succeed.");
    }
}
