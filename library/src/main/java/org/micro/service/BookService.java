package org.micro.service;



import org.micro.dto.BookDTO;
import org.micro.model.Book;

import java.util.List;

public interface BookService {

    List<Book> getAll();
    Book addBook(BookDTO book);
    Book findById(Integer id);
    Book update(Book book);
    Boolean delete(Integer id);
    Book getByName(String name);
    List<Book> getBooksByAuthor_Id(Integer author_id);
    List<Book> getBooksByBookType_Type_id(Integer type_id);
    List<Book> getBooksByIdIsIn(List<Integer> ids);
}
