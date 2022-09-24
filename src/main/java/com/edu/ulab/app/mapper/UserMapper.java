package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.PersonDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.web.request.UserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    PersonDto userRequestToUserDto(UserRequest userRequest);

    UserRequest userDtoToUserRequest(PersonDto userDto);

    Person userDtoToPerson(PersonDto userDto);

    PersonDto personToUserDto(Person person);
}
