package org.micro.service.impl;


import org.micro.dto.BookDTO;
import org.micro.model.Author;
import org.micro.model.Book;
import org.micro.model.BookType;
import org.micro.repository.AuthorRepository;
import org.micro.repository.BookRepository;
import org.micro.repository.BookTypeRepository;
import org.micro.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookTypeRepository bookTypeRepository;

    @Override
    public List<Book> getAll() {
        try {
            return bookRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Book addBook(BookDTO source) {
        Book target = new Book();
        if(source.getAuthor_id() == null || source.getType_id() == null) {
            return null;
        }

        Author author = authorRepository.findById(source.getAuthor_id()).orElse(null);
        BookType bookType = bookTypeRepository.findById(source.getType_id()).orElse(null);

        if(author != null && bookType != null) {
            target.setAuthor(author);
            target.setBookType(bookType);
            target.setName(source.getName());
            target.setContent(source.getContent());

            if(source.getId() != null && !source.getId().equals(0)) {
                target.setId(source.getId());
            }

            return bookRepository.save(target);
        }

        return null;
    }

    @Override
    public Book findById(Integer id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public Book update(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Boolean delete(Integer id) {
        try {
            bookRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Book getByName(String name) {
        try {
            return bookRepository.getBookByName(name);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Book> getBooksByAuthor_Id(Integer author_id) {
        try {
            return bookRepository.getBooksByAuthor_Id(author_id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Book> getBooksByBookType_Type_id(Integer type_id) {
        try {
            return bookRepository.getBooksByBookType_Type_id(type_id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Book> getBooksByIdIsIn(List<Integer> ids) {
        try {
            return bookRepository.getBooksByIdIsIn(ids);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Book> getBooksByPage(Integer limit, Integer offset) {
        try {
            return bookRepository.getBooksByPage(limit, offset);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
