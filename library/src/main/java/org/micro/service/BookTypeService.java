package org.micro.service;



import org.micro.model.BookType;

import java.util.List;

public interface BookTypeService {

    List<BookType> getAll();
    BookType addBookType(BookType book);
    BookType findById(Integer id);
    Boolean delete(Integer id);
    BookType getByName(String name);
}
