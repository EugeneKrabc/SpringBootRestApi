package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.PersonDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public PersonDto createUser(PersonDto userDto) {
        Person user = userMapper.userDtoToPerson(userDto);
        log.info("Mapped user: {}", user);
        Person savedUser = userRepository.save(user);
        log.info("Saved user: {}", savedUser);
        return userMapper.personToUserDto(savedUser);
    }

    @Override
    public void updateUser(PersonDto userDto) {
        // реализовать недстающие методы
    }

    @Override
    public PersonDto getUserById(Long id) {
        // реализовать недстающие методы
        return null;
    }

    @Override
    public void deleteUserById(Long id) {
        // реализовать недстающие методы
    }
}
