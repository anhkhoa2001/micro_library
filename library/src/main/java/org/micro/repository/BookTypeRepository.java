package org.micro.repository;

import org.micro.model.BookType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookTypeRepository extends JpaRepository<BookType, Integer> {

    BookType getBookTypeByName(String name);
}
