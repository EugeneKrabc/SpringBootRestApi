package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Person;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

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

    @DisplayName("Сохранить юзера. Число select должно равняться 1")
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
        Person result = userRepository.save(person);

        //Then
        assertThat(result.getAge()).isEqualTo(24);
        assertThat(result.getFullName().equals("Evgeniy"));
        assertThat(result.getTitle().equals("reader"));
        assertThat(result.getId() == 1);
        assertSelectCount(0);
        assertInsertCount(1);
        assertUpdateCount(0);
        assertDeleteCount(0);
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
        assertThat(existentPerson.getId().equals(existentId));
        assertThat(existentPerson.getFullName().equals("Test User"));
        assertThat(existentPerson.getTitle().equals("Tester"));
        assertThat(existentPerson.getAge() == 21);
        assertThat(nonExistentPerson == null);
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);

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
        assertThat(deletedPerson == null);
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
        userRepository.save(updatedPerson);
        Person receivedPerson = userRepository.findById(existentUserId).orElse(null);

        //Then
        assertThat(receivedPerson.getAge() == 99);
        assertThat(receivedPerson.getTitle().equals("updater"));
        assertThat(receivedPerson.getFullName().equals("Updated User"));
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }
    // update
    // get
    // get all
    // delete

    // * failed
}
