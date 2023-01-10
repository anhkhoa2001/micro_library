package org.micro.service.impl;

import org.micro.model.BorrowBook;
import org.micro.repository.BorrowBookRepository;
import org.micro.service.BorrowBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.Date;
import java.util.List;

@Service
public class BorrowBookServiceImpl implements BorrowBookService {

    @Autowired
    private BorrowBookRepository borrowBookRepository;

    @Override
    public BorrowBook add(BorrowBook borrowBook) {
        try {
            return borrowBookRepository.save(borrowBook);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<BorrowBook> getAll() {
        try {
            return borrowBookRepository.findAllByIs_borrow();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public BorrowBook findById(Integer id) {
        try {
            return borrowBookRepository.findById(id).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long countByTimer(Date start, Date end) {
        try {
            return borrowBookRepository.countByTimer(start, end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -2001;
    }

    @Override
    public long getBookIdByTimer(Date start, Date end) {
        try {
            return borrowBookRepository.getBookIdByTimer(start, end);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -2001;
    }
}
