package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты репозитория {@link BookRepository}.
 */
@SystemJpaTest
public class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить книгу.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void saveBookAndPerson_thenAssertDmlCount() {
        //Given
        Long personId = 1001L;

        Book book = new Book();
        book.setAuthor("Test Author");
        book.setTitle("test");
        book.setPageCount(1000);
        book.setPersonId(personId);

        //When
        Book result = bookRepository.saveAndFlush(book);

        //Then
        assertThat(result.getPageCount()).isEqualTo(1000);
        assertThat(result.getTitle()).isEqualTo("test");
        assertThat(result.getId()).isEqualTo(100);
        assertSelectCount(1);
        assertInsertCount(1);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Обновить информацию о существующей книге.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updateBook_thenAssertDmlCount() {
        //Given
        Book updatedBook = new Book();
        updatedBook.setId(2002L);
        updatedBook.setAuthor("Updated Author");
        updatedBook.setTitle("Updated Title");
        updatedBook.setPageCount(999);
        updatedBook.setPersonId(1001L);

        //When
        Book receiveBook = bookRepository.saveAndFlush(updatedBook);

        //Then
        assertThat(receiveBook.getId()).isEqualTo(2002);
        assertThat(receiveBook.getTitle()).isEqualTo("Updated Title");
        assertThat(receiveBook.getAuthor()).isEqualTo("Updated Author");
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(1);
        assertDeleteCount(0);

    }

    @DisplayName("Получить книгу по её id.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getBook_thenAssertDmlCount() {
        //Given
        Long existentBookId = 2002L;
        Long nonExistentBookId = 999L;

        //When
        Book existentBook = bookRepository.findById(existentBookId).orElse(null);
        Book nonExistentBook = bookRepository.findById(nonExistentBookId).orElse(null);

        //Then
        assertThat(existentBook).isNotNull();
        assertThat(existentBook.getId()).isEqualTo(existentBookId);
        assertThat(existentBook.getTitle()).isEqualTo("Thinking in Java");
        assertThat(existentBook.getPageCount()).isEqualTo(5500);
        assertThat(nonExistentBook).isNull();
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Найти список id книг по id юзера.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getBookIdListWithPersonId_thenAssertDmlCount() {
        //Given
        Long personId = 1001L;

        //When
        List<Long> bookIdList = bookRepository.getBookIdListWithUserId(personId).stream().toList();

        //Then
        assertThat(bookIdList).isEqualTo(Arrays.asList(2002L, 3003L));
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Удалить книгу по её id.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deleteBookByID_thenAssertDmlCount() {
        //Given
        Long bookId = 3003L;

        //When
        bookRepository.deleteById(bookId);
        Book deletedBook = bookRepository.findById(bookId).orElse(null);

        //Then
        assertThat(deletedBook).isNull();
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Удалить книги определнного юзера.")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deleteBooksWithUserId_thenAssertDmlCount() {
        //Given
        Long personId = 1001L;

        //When
        bookRepository.deleteByPersonId(personId);
        List<Long> bookIdList = bookRepository.getBookIdListWithUserId(personId).stream().toList();

        //Then
        assertThat(bookIdList).isEmpty();
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(2);
    }
}
