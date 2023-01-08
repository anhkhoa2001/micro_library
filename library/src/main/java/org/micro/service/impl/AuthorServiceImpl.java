package org.micro.service.impl;

import org.micro.model.Author;
import org.micro.repository.AuthorRepository;
import org.micro.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    @Autowired
    private AuthorRepository authorRepository;
    @Override
    public List<Author> getAll() {
        return authorRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public Author addAuthor(Author book) {
        try {
            return authorRepository.save(book);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Author findById(Integer id) {
        return authorRepository.findById(id).orElse(null);
    }

    @Override
    public Boolean delete(Integer id) {
        try {
            authorRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Author getByName(String name) {
        return authorRepository.getAuthorByName(name);
    }
}
