package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.impl.BookServiceImplJpa;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.BookServiceImplJpa}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing book functionality.")
public class BookServiceImplTest {
    @InjectMocks
    BookServiceImplJpa bookService;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookMapper bookMapper;

    @Test
    @DisplayName("Создание книги.")
    void saveBook_Test() {
        //given
        Long personId = 1L;

        BookDto bookDto = new BookDto();
        bookDto.setPersonId(personId);
        bookDto.setAuthor("test author");
        bookDto.setTitle("test title");
        bookDto.setPageCount(1000);

        BookDto result = new BookDto();
        result.setId(1L);
        result.setPersonId(1L);
        result.setAuthor("test author");
        result.setTitle("test title");
        result.setPageCount(1000);

        Book book = new Book();
        book.setPageCount(1000);
        book.setTitle("test title");
        book.setAuthor("test author");
        book.setPersonId(personId);

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setPageCount(1000);
        savedBook.setTitle("test title");
        savedBook.setAuthor("test author");
        savedBook.setPersonId(personId);

        //when

        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);


        //then
        BookDto bookDtoResult = bookService.createBook(bookDto);
        assertEquals(1L, bookDtoResult.getId());
        assertEquals("test title", bookDtoResult.getTitle());
    }

    @Test
    @DisplayName("Получение книги по id.")
    void getBook_test() {
        //Given

        Book book = new Book();
        book.setPageCount(1000);
        book.setTitle("test title");
        book.setAuthor("test author");
        book.setPersonId(123L);
        book.setId(1L);

        BookDto bookDto = new BookDto();
        bookDto.setPersonId(123L);
        bookDto.setAuthor("test author");
        bookDto.setTitle("test title");
        bookDto.setPageCount(1000);
        bookDto.setId(1L);

        //When

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.bookToBookDto(book)).thenReturn(bookDto);

        //Then

        BookDto bookDtoResult = bookService.getBookById(1L);
        assertEquals("test title", bookDtoResult.getTitle());
    }

    @Test
    @DisplayName("Получение списка книг пользователя.")
    void getBookIdList_test() {
        //Given
        Long userId = 123L;

        //When

        when(bookRepository.getBookIdListWithUserId(userId)).thenReturn(Arrays.asList(1L, 2L, 3L));

        //Then
        List<Long> bookIdList = bookService.getBookIdListForUser(123L);
        assertEquals(Arrays.asList(1L, 2L, 3L), Arrays.asList(1L, 2L, 3L));
    }

    @Test
    @DisplayName("Получение списка книг пользователя.")
    void deleteBookById_test() {
        //Given

        BookDto bookDtoToSave = new BookDto();
        bookDtoToSave.setTitle("Title");
        bookDtoToSave.setAuthor("Author");
        bookDtoToSave.setPageCount(100);
        bookDtoToSave.setPersonId(33L);

        Book bookToSave = new Book();
        bookToSave.setTitle("Title");
        bookToSave.setAuthor("Author");
        bookToSave.setPageCount(100);
        bookToSave.setPersonId(33L);

        Book savedBook = new Book();
        savedBook.setTitle("Title");
        savedBook.setAuthor("Author");
        savedBook.setPageCount(100);
        savedBook.setPersonId(33L);
        savedBook.setId(1L);

        BookDto savedBookDto = new BookDto();
        savedBookDto.setTitle("Title");
        savedBookDto.setAuthor("Author");
        savedBookDto.setPageCount(100);
        savedBookDto.setPersonId(33L);
        savedBookDto.setId(1L);

        //When

        when(bookMapper.bookDtoToBook(bookDtoToSave)).thenReturn(bookToSave);
        when(bookRepository.save(bookToSave)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(savedBookDto);
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        when(bookMapper.bookToBookDto(null)).thenReturn(null);

        //Then

        bookService.createBook(bookDtoToSave);
        bookService.deleteBookById(33L);
        assertNull(bookService.getBookById(1L));
    }

    @Test
    @DisplayName("Удаление книг с определенным пользователем.")
    void deleteBooksWithUserId_test() {
        //Given

        Long userId = 123L;

        //When

        when(bookRepository.getBookIdListWithUserId(userId)).thenReturn(Collections.emptyList());

        //Then
        bookService.deleteBooksWithUserId(userId);
        List<Long> bookIdList = bookService.getBookIdListForUser(userId);
        assertTrue(bookIdList.isEmpty());
    }

    @Test
    @DisplayName("Обновление информации о книге.")
    void updateBook_test() {
        //Given
        BookDto updatedBookDto = new BookDto();
        updatedBookDto.setId(1L);
        updatedBookDto.setTitle("updated title");
        updatedBookDto.setAuthor("updated author");
        updatedBookDto.setPageCount(999);
        updatedBookDto.setPersonId(123L);

        Book updatedBook = new Book();
        updatedBook.setId(1L);
        updatedBook.setTitle("updated title");
        updatedBook.setAuthor("updated author");
        updatedBook.setPageCount(999);
        updatedBook.setPersonId(123L);

        //When

        when(bookMapper.bookDtoToBook(updatedBookDto)).thenReturn(updatedBook);
        when(bookRepository.findById(updatedBookDto.getId())).thenReturn(Optional.of(updatedBook));
        when(bookMapper.bookToBookDto(updatedBook)).thenReturn(updatedBookDto);

        //Then
        bookService.updateBook(updatedBookDto);
        BookDto resultDto = bookService.getBookById(updatedBookDto.getId());
        assertEquals(updatedBookDto.getId(), resultDto.getId());
        assertEquals(updatedBookDto.getTitle(), resultDto.getTitle());
    }

}
