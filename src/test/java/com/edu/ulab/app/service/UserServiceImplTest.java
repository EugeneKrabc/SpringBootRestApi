package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.UserServiceImplJpa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.UserServiceImplJpa}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing user functionality.")
public class UserServiceImplTest {
    @InjectMocks
    UserServiceImplJpa userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Test
    @DisplayName("Создание пользователя.")
    void savePerson_Test() {
        //Given

        UserDto userDto = new UserDto();
        userDto.setAge(11);
        userDto.setFullName("test name");
        userDto.setTitle("test title");

        Person person  = new Person();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");

        Person savedPerson  = new Person();
        savedPerson.setId(100L);
        savedPerson.setFullName("test name");
        savedPerson.setAge(11);
        savedPerson.setTitle("test title");

        UserDto result = new UserDto();
        result.setId(100L);
        result.setAge(11);
        result.setFullName("test name");
        result.setTitle("test title");


        //When

        when(userMapper.userDtoToPerson(userDto)).thenReturn(person);
        when(userRepository.save(person)).thenReturn(savedPerson);
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);


        //Then

        UserDto userDtoResult = userService.createUser(userDto);
        assertEquals(100L, userDtoResult.getId());
    }

    @Test
    @DisplayName("Получение пользователя.")
    void getUser_test() {
        //Given

        Long userId = 1001L;

        Person person = new Person();
        person.setAge(21);
        person.setFullName("Test User");
        person.setTitle("Tester");
        person.setId(userId);

        UserDto userDto = new UserDto();
        userDto.setAge(21);
        userDto.setFullName("Test User");
        userDto.setTitle("Tester");
        userDto.setId(userId);

        //When

        when(userRepository.findById(userId)).thenReturn(Optional.of(person));
        when(userMapper.personToUserDto(person)).thenReturn(userDto);

        //Then

        UserDto userDtoResult = userService.getUserById(userId);
        assertEquals("Test User", userDtoResult.getFullName());
        assertEquals(21, userDtoResult.getAge());
        assertEquals("Tester", userDtoResult.getTitle());
    }

    @Test
    @DisplayName("Обновление пользователя.")
    void updateUser_test() {
        //Given

        UserDto userDtoToSave = new UserDto();
        userDtoToSave.setAge(99);
        userDtoToSave.setFullName("Old Person");
        userDtoToSave.setTitle("grandfather");

        Person beforeUpdatePerson = new Person();
        beforeUpdatePerson.setFullName("Old Person");
        beforeUpdatePerson.setTitle("grandfather");
        beforeUpdatePerson.setAge(100);

        Person savedBeforeUpdatePerson = new Person();
        savedBeforeUpdatePerson.setFullName("Old Person");
        savedBeforeUpdatePerson.setTitle("grandfather");
        savedBeforeUpdatePerson.setAge(100);
        savedBeforeUpdatePerson.setId(1L);

        UserDto userDtoToUpdate = new UserDto();
        userDtoToUpdate.setId(1L);
        userDtoToUpdate.setAge(18);
        userDtoToUpdate.setFullName("updated name");
        userDtoToUpdate.setTitle("updated title");

        Person updatedPerson  = new Person();
        updatedPerson.setFullName("updated name");
        updatedPerson.setAge(18);
        updatedPerson.setTitle("updated title");
        updatedPerson.setId(1L);

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setFullName("updated name");
        updatedUserDto.setTitle("updated title");
        updatedUserDto.setAge(18);
        updatedUserDto.setId(1L);

        //When
        when(userMapper.userDtoToPerson(userDtoToSave)).thenReturn(beforeUpdatePerson);
        when(userRepository.save(beforeUpdatePerson)).thenReturn(savedBeforeUpdatePerson);
        when(userMapper.userDtoToPerson(userDtoToUpdate)).thenReturn(updatedPerson);
        when(userRepository.save(updatedPerson)).thenReturn(updatedPerson);
        when(userRepository.findById(1L)).thenReturn(Optional.of(updatedPerson));
        when(userMapper.personToUserDto(updatedPerson)).thenReturn(updatedUserDto);


        //Then
        userService.createUser(userDtoToSave);
        userService.updateUser(userDtoToUpdate);
        assertEquals(userService.getUserById(1L).getFullName(), "updated name");

    }

    @Test
    @DisplayName("Удаление пользователя.")
    void deleteUser_test() {
        //Given

        UserDto userDtoToSave = new UserDto();
        userDtoToSave.setFullName("Username");
        userDtoToSave.setTitle("title");
        userDtoToSave.setAge(21);

        Person personToSave = new Person();
        personToSave.setFullName("Username");
        personToSave.setTitle("title");
        personToSave.setAge(21);

        Person savedPerson = new Person();
        savedPerson.setFullName("Username");
        savedPerson.setTitle("title");
        savedPerson.setAge(21);
        savedPerson.setId(1L);

        UserDto savedUserDto = new UserDto();
        savedUserDto.setFullName("Username");
        savedUserDto.setTitle("title");
        savedUserDto.setAge(21);
        savedUserDto.setId(1L);

        //When
        when(userMapper.userDtoToPerson(userDtoToSave)).thenReturn(personToSave);
        when(userRepository.save(personToSave)).thenReturn(savedPerson);
        when(userMapper.personToUserDto(savedPerson)).thenReturn(savedUserDto);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        //Then
        userService.createUser(userDtoToSave);
        userService.deleteUserById(1L);
        assertNull(userService.getUserById(1L));

    }

    // update
    // get
    // get all
    // delete

    // * failed
    //         doThrow(dataInvalidException).when(testRepository)
    //                .save(same(test));
    // example failed
    //  assertThatThrownBy(() -> testeService.createTest(testRequest))
    //                .isInstanceOf(DataInvalidException.class)
    //                .hasMessage("Invalid data set");
}
