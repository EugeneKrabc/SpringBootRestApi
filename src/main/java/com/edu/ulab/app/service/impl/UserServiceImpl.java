package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.exception.IncorrectDataException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.StorageDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    StorageDAO storageDAO;

    UserMapper userMapper;

    @Autowired
    public void setStorageDAO(StorageDAO storageDAO) {
        this.storageDAO = storageDAO;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        checkCorrectDataInUserDto(userDto);

        Long savedUserId = storageDAO.saveUser(userMapper.userDtoToUserEntity(userDto));
        userDto.setId(savedUserId);

        return userDto;
    }

    @Override
    public UserDto getUserById(Long userId) {
        UserDto userDto = userMapper.userEntityToUserDto(storageDAO.getUserById(userId));
        userDto.setId(userId);
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        checkCorrectDataInUserDto(userDto);

        storageDAO.updateUser(userMapper.userDtoToUserEntity(userDto), userId);

        return getUserById(userId);
    }

    @Override
    public void deleteUserWithBooksById(Long userId) {
        storageDAO.deleteUserWithBooksById(userId);
    }

    private void checkCorrectDataInUserDto(UserDto userDto) {
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
