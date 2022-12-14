package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Person;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;

import java.sql.SQLException;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Тесты репозитория {@link UserRepository}.
 */
@SystemJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить юзера в бд.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void insertPerson_thenAssertDmlCount() {
        //Given

        Person person = new Person();
        person.setAge(24);
        person.setTitle("reader");
        person.setFullName("Evgeniy");

        //When

        Person result = userRepository.saveAndFlush(person);

        //Then

        assertThat(result.getAge()).isEqualTo(24);
        assertThat(result.getFullName()).isEqualTo("Evgeniy");
        assertThat(result.getTitle()).isEqualTo("reader");
        assertSelectCount(1);
        assertInsertCount(1);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Сохранение юзера с неуникальной должностью, должно выброситься исключение.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void insertPersonWithNonUniqueTitleThanException_Test() {
        //Given

        Person person = new Person();
        person.setAge(24);
        person.setTitle("Tester");
        person.setFullName("Evgeniy");

        assertThatThrownBy(()-> userRepository.saveAndFlush(person))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("Получить юзера по его id")
    @Test
    @Rollback
    @Sql(value = {"/sql/1_clear_schema.sql",
            "/sql/2_insert_person_data.sql",
            "/sql/3_insert_book_data.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getPerson_thenAssertDmlCount() {
        //Given

        Long existentId = 1001L;
        Long nonExistentId = 999L;

        //When

        Person existentPerson = userRepository.findById(existentId).orElse(null);
        Person nonExistentPerson = userRepository.findById(nonExistentId).orElse(null);

        //Then

        assertThat(existentPerson).isNotNull();
        assertThat(existentPerson.getId()).isEqualTo(existentId);
        assertThat(existentPerson.getFullName()).isEqualTo("Test User");
        assertThat(existentPerson.getTitle()).isEqualTo("Tester");
        assertThat(existentPerson.getAge()).isEqualTo(21);
        assertThat(nonExistentPerson).isNull();
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);

    }

    @DisplayName("Получение пользователя при id=null, должно выброситься исключение")
    @Test
    @Rollback
    @Sql(value = {"/sql/1_clear_schema.sql",
            "/sql/2_insert_person_data.sql",
            "/sql/3_insert_book_data.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getPersonByNullIdThenException_Test() {
        //Given

        Long nullId = null;

        assertThatThrownBy(()-> userRepository.findById(nullId).orElse(null))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);

    }

    @DisplayName("Удалить юзера по его id")
    @Test
    @Rollback
    @Sql(value = {"/sql/1_clear_schema.sql",
            "/sql/2_insert_person_data.sql",
            "/sql/3_insert_book_data.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deletePerson_thenAssertDmlCount() {
        //Given

        Long userId = 1001L;

        //When

        userRepository.deleteById(userId);
        Person deletedPerson = userRepository.findById(userId).orElse(null);

        //Then

        assertThat(deletedPerson).isNull();
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Обновить информацию о существующем юзере")
    @Test
    @Rollback
    @Sql(value = {"/sql/1_clear_schema.sql",
            "/sql/2_insert_person_data.sql",
            "/sql/3_insert_book_data.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updatePerson_thenAssertDmlCount() {
        //Given

        Long existentUserId = 1001L;
        Person updatedPerson = new Person();
        updatedPerson.setId(existentUserId);
        updatedPerson.setFullName("Updated User");
        updatedPerson.setTitle("updater");
        updatedPerson.setAge(99);

        //When

        userRepository.saveAndFlush(updatedPerson);
        Person receivedPerson = userRepository.findById(existentUserId).orElse(null);

        //Then

        assertThat(receivedPerson).isNotNull();
        assertThat(receivedPerson.getAge()).isEqualTo(99);
        assertThat(receivedPerson.getTitle()).isEqualTo("updater");
        assertThat(receivedPerson.getFullName()).isEqualTo("Updated User");
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(1);
        assertDeleteCount(0);
    }

    @DisplayName("Обновление пользователя с null плями, должно выброситься исключение")
    @Test
    @Rollback
    @Sql(value = {"/sql/1_clear_schema.sql",
            "/sql/2_insert_person_data.sql",
            "/sql/3_insert_book_data.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void updatePersonWithNullColumnThenException_test() {
        //Given

        Long existentUserId = 1001L;
        Person updatedPerson = new Person();
        updatedPerson.setId(existentUserId);
        updatedPerson.setFullName(null);
        updatedPerson.setTitle(null);
        updatedPerson.setAge(99);

        assertThatThrownBy(()-> userRepository.saveAndFlush(updatedPerson))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}
