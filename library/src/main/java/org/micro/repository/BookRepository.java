package org.micro.repository;

import org.micro.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Book getBookByName(String name);
    List<Book> getBooksByAuthor_Id(Integer author_id);
    @Query(value="select * from li_book lb where lb.book_type_id= :type_id", nativeQuery=true)
    List<Book> getBooksByBookType_Type_id(Integer type_id);
    List<Book> getBooksByIdIsIn(List<Integer> ids);
}
