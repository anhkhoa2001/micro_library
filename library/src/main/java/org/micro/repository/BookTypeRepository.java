package org.micro.repository;

import org.micro.model.Author;
import org.micro.model.BookType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookTypeRepository extends JpaRepository<BookType, Integer> {
    BookType getBookTypeByName(String name);
    @Query(value="select * from rest_book_type lb limit :lim offset :off", nativeQuery=true)
    List<BookType> getBookTypesByPage(@Param("lim") Integer limit, @Param("off") Integer offset);
}
