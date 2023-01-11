package org.micro.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.micro.model.BookType;
import org.micro.repository.BookTypeRepository;
import org.micro.service.BookTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class BookTypeServiceImpl implements BookTypeService {
    @Autowired
    private BookTypeRepository bookTypeRepository;

    @Override
    public List<BookType> getAll() {
        return bookTypeRepository.findAll();
    }

    @Override
    public BookType addBookType(BookType bookType) {
        try {
            return bookTypeRepository.save(bookType);
        } catch (DataIntegrityViolationException e) {
            return new BookType();
        }
    }

    @Override
    public BookType findById(Integer id) {
        return bookTypeRepository.findById(id).orElse(null);
    }

    @Override
    public Boolean delete(Integer id) {
        try {
            bookTypeRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public BookType getByName(String name) {
        try {
            return bookTypeRepository.getBookTypeByName(name);
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<BookType> getBookTypesByPage(Integer limit, Integer offset) {
        try {
            return bookTypeRepository.getBookTypesByPage(limit, offset);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
