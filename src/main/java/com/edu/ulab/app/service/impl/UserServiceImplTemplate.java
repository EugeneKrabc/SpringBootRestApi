package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dao.PersonDAO;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImplTemplate implements UserService {

    private final PersonDAO personDAO;

    public UserServiceImplTemplate(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setId(personDAO.savePerson(userDto));
        return userDto;
    }

    @Override
    public void updateUser(UserDto userDto) {
        personDAO.updateUser(userDto);
    }

    @Override
    public UserDto getUserById(Long id) {
        return personDAO.getUserById(id);
    }

    @Override
    public void deleteUserById(Long id) {
        personDAO.deleteUser(id);
    }
}
