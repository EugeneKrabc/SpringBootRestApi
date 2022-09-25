package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.IncorrectDataException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImplJpa implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImplJpa(UserRepository userRepository,
                              UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        checkCorrectDataInUserDto(userDto);
        Person user = userMapper.userDtoToPerson(userDto);
        log.info("Mapped user: {}", user);
        Person savedUser = userRepository.save(user);
        log.info("Saved user: {}", savedUser);
        return userMapper.personToUserDto(savedUser);
    }

    @Override
    public void updateUser(UserDto userDto) {
        checkCorrectDataInUserDto(userDto);
        userRepository.save(userMapper.userDtoToPerson(userDto));
    }

    @Override
    public UserDto getUserById(Long id) {
        return userMapper.personToUserDto(userRepository.findById(id).orElse(null));
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
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
