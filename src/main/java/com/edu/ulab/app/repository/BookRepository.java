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
    @Query(value = "SELECT ID from ulab_edu.book WHERE person_id = ?1",
            nativeQuery = true)
    Collection<Long> getBookIdListWithUserId(Long userId);

    @Transactional
    void deleteByPersonId(Long personId);
}
