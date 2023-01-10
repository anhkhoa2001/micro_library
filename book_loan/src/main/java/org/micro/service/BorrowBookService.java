package org.micro.service;

import org.micro.model.BorrowBook;

import java.awt.print.Book;
import java.util.Date;
import java.util.List;

public interface BorrowBookService {
    BorrowBook add(BorrowBook borrowBook);

    List<BorrowBook> getAll();

    BorrowBook findById(Integer id);

    long countByTimer(Date start, Date end);

    long getBookIdByTimer(Date start, Date end);
}
