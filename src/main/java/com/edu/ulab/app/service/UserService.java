package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.PersonDto;
import org.springframework.stereotype.Repository;

@Repository
public interface UserService {
    PersonDto createUser(PersonDto userDto);

    void updateUser(PersonDto userDto);

    PersonDto getUserById(Long id);

    void deleteUserById(Long id);
}
