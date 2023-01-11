package org.micro.service;

import org.micro.model.Author;

import java.util.List;

public interface AuthorService {
    List<Author> getAll();
    Author addAuthor(Author author);
    Author findById(Integer id);
    Boolean delete(Integer id);
    Author getByName(String name);

    List<Author> getAuthorsByPage(Integer limit, Integer offset);
}
