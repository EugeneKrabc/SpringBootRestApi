package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.entity.UserEntity;

import java.util.List;

public interface StorageDAO {
    Long saveUser(UserEntity userEntity);
    Long saveBook(BookEntity bookEntity);
    List<Long> getBookIdListByUserId(Long userId);
    void updateUser(UserEntity userEntity, Long userId);
    void deleteUserWithBooksById(Long userId);
    UserEntity getUserById(Long userId);
}
