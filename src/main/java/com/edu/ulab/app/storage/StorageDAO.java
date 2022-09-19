package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.entity.UserEntity;

import java.util.List;

public interface StorageDAO {
    public Long saveUser(UserEntity userEntity);
    public Long saveBook(BookEntity bookEntity);
    public List<Long> getBookIdListByUserId(Long userId);
    public void updateUser(UserEntity userEntity, Long userId);
    public void deleteUserWithBooksById(Long userId);
    public UserEntity getUserById(Long userId);
}
