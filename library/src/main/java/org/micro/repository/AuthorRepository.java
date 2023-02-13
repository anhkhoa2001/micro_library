package org.micro.repository;

import org.micro.model.Author;
import org.micro.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Author getAuthorByName(String name);
    @Query(value="select * from rest_author lb limit :lim offset :off", nativeQuery=true)
    List<Author> getAuthorsByPage(@Param("lim") Integer limit, @Param("off") Integer offset);

}
