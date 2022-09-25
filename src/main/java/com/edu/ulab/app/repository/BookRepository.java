package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.Book;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Query("select b from Book b where b.id = :id")
//    Optional<Book> findByIdForUpdate(long id);
    @Query(value = "SELECT ID from Book WHERE USER_ID = ?1",
            nativeQuery = true)
    Collection<Long> getBookIdListWithUserId(Long userId);

    @Transactional
    void deleteByUserId(Long userId);
}
