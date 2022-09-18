package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    Storage storage;

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setId(storage.saveUser(userDto));
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        storage.updateUser(userDto, userId);
        userDto.setId(userId);
        return userDto;
    }

    @Override
    public UserDto getUserById(Long userId) {
//        UserEntity userWithBooks = storage.findUserById(userId);
//        UserDto userDto = new UserDto();
//
//        userDto.setFullName(userWithBooks.getFullName());
//        userDto.setTitle(userWithBooks.getTitle());
//        userDto.setAge(userWithBooks.getAge());
//        userDto.setId(userId);

        return null;
    }

    @Override
    public void deleteUserById(Long id) {
        storage.deleteUserWithBooksById(id);
    }
}
