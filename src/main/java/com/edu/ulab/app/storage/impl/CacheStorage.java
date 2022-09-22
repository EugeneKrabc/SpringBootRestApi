package com.edu.ulab.app.storage.impl;

import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.storage.StorageDAO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CacheStorage implements StorageDAO {

    Map<Long, UserEntity> users = new HashMap<>();
    Map<Long, BookEntity> books = new HashMap<>();

    private static long uniqUserId = 1;
    private static long uniqBookId = 1;

    @Override
    public Long saveUser(UserEntity userEntity) {
        Long userId = uniqUserId++;

        users.put(userId, userEntity);

        return userId;
    }

    @Override
    public Long saveBook(BookEntity bookEntity) {
        Long bookId = uniqBookId++;
        Long userId = bookEntity.getUserId();

        books.put(bookId, bookEntity);
        users.get(userId).getBookIdSet().add(bookId);

        return bookId;
    }

    @Override
    public List<Long> getBookIdListByUserId(Long userId) {
        checkIfUserPresent(userId);
        return users.get(userId).getBookIdSet().stream().toList();
    }

    @Override
    public void updateUser(UserEntity updatedUser, Long userId) {
        checkIfUserPresent(userId);

        removeBooksWithUserId(userId);

        users.replace(userId, updatedUser);
    }

    @Override
    public void deleteUserWithBooksById(Long userId) {
        checkIfUserPresent(userId);

        removeBooksWithUserId(userId);

        users.remove(userId);
    }

    @Override
    public UserEntity getUserById(Long userId) {
        checkIfUserPresent(userId);
        return users.get(userId);
    }

    @Override
    public BookEntity getBookByID(Long bookId) {
        checkIfBookPresent(bookId);
        return books.get(bookId);
    }

    @Override
    public void deleteBookById(Long BookId) {
        checkIfBookPresent(BookId);
        books.remove(BookId);

    }

    private void checkIfUserPresent(Long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("There is no user with id = " + userId);
        }
    }

    private void checkIfBookPresent(Long bookId) {
        if (!books.containsKey(bookId)) {
            throw new NotFoundException("There is no book with id = " + bookId);
        }
    }

    private void removeBooksWithUserId(Long userId) {
        users.get(userId).getBookIdSet().forEach(bookId -> books.remove(bookId));
    }
}
