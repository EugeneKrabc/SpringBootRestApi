package com.edu.ulab.app.storage;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.BookEntity;
import com.edu.ulab.app.entity.UserEntity;
import com.edu.ulab.app.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Storage {
    //todo создать хранилище в котором будут содержаться данные
    // сделать абстракции через которые можно будет производить операции с хранилищем
    // продумать логику поиска и сохранения
    // продумать возможные ошибки
    // учесть, что при сохранеии юзера или книги, должен генерироваться идентификатор
    // продумать что у узера может быть много книг и нужно создать эту связь
    // так же учесть, что методы хранилища принимают друго тип данных - учесть это в абстракции

    Map<Long, UserEntity> users = new HashMap<>();
    Map<Long, BookEntity> books = new HashMap<>();

    private static long uniqUserId = 1;
    private static long uniqBookId = 1;

    public Long saveUser(UserDto userDto) {
        long savedUserId = uniqUserId++;

        users.put(savedUserId,
                new UserEntity(userDto.getFullName(), userDto.getTitle(), userDto.getAge()));

        return savedUserId;
    }

    public long saveBook(BookDto bookDto) {
        long savedBookId = uniqBookId++;
        long userId = bookDto.getUserId();

        BookEntity book = new BookEntity(
                bookDto.getTitle(), bookDto.getAuthor(), bookDto.getPageCount(), userId
        );
        books.put(savedBookId, book);

        users.get(userId).addBookId(savedBookId);
        return savedBookId;
    }

    public List<Long> getBookIdListByUserId(Long userId) {
        checkIfUserPresent(userId);

        return users.get(userId).getBookIdSet().stream().toList();
    }

    public void updateUser(UserDto userDto, Long userId) {
        checkIfUserPresent(userId);

        removeBooksByUserId(userId);

        UserEntity updatedUser = new UserEntity(userDto.getFullName(), userDto.getTitle(), userDto.getAge());

        users.replace(userId, updatedUser);
    }

    public void deleteUserWithBooksById(Long userId) {
        checkIfUserPresent(userId);

        removeBooksByUserId(userId);

        users.remove(userId);
    }

    private void removeBooksByUserId(Long userId) {
        users.get(userId).getBookIdSet().forEach(bookId -> books.remove(bookId));
    }

    private void checkIfUserPresent(Long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("There is no user with id = " + userId);
        }
    }
}
