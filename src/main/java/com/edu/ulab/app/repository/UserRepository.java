package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.Person;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface UserRepository extends CrudRepository<Person, Long> {

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Query("select p from Person p where p.id = :id")
//    Optional<Person> findByIdForUpdate(long id);
//    jdbcTemplate.update("UPDATE PERSON SET FULL_NAME = ?, TITLE = ?, AGE = ? WHERE ID = ?",
//            userDto.getFullName(), userDto.getTitle(), userDto.getAge(), userDto.getId());

}
