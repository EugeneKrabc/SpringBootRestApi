package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.exception.IncorrectDataException;
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
        checkCorrectUserDto(userDto);
        userDto.setId(storage.saveUser(userDto));
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        checkCorrectUserDto(userDto);
        storage.updateUser(userDto, userId);
        return storage.getUserById(userId);
    }

    @Override
    public UserDto getUserById(Long userId) {
        return storage.getUserById(userId);
    }

    @Override
    public void deleteUserById(Long userId) {
        storage.deleteUserWithBooksById(userId);
    }

    private void checkCorrectUserDto(UserDto userDto) {
        if (userDto.getFullName() == null || userDto.getFullName().length() == 0) {
            throw new IncorrectDataException("User name is null or empty");
        }

        if (userDto.getTitle() == null || userDto.getTitle().length() == 0) {
            throw new IncorrectDataException("User title is null or empty");
        }

        if (userDto.getAge() <= 0) {
            throw new IncorrectDataException("Invalid user age = " + userDto.getAge());
        }
    }


}
