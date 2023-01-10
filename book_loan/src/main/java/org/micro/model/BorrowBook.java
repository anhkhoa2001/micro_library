package org.micro.model;

import lombok.Data;
import org.micro.enums.StatusBorrowBook;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mbl_borrow_book")
@Data
public class BorrowBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer user_id;

    @Column(name = "book_id")
    private Integer book_id;

    //is_borrow == 1 là ngày mượn
    //is_borrow == 0 là ngày trả
    //ngày trả sẽ phải trả đúng id user và book đã mượn
    @Column(name = "is_borrow")
    private Boolean is_borrow;

    @Column(name = "start_date")
    private Date start_date;

    @Column(name = "due_date")
    private Date due_date;

    //mượn sách không cần thuộc tính này
    @Column(name = "return_date")
    private Date return_date;
    @Column(name = "status")
    private StatusBorrowBook status;

    public BorrowBook(Integer user_id, Integer book_id, Boolean is_borrow, Date start_date, Date due_date, StatusBorrowBook status) {
        this.user_id = user_id;
        this.book_id = book_id;
        this.is_borrow = is_borrow;
        this.start_date = start_date;
        this.due_date = due_date;
        this.status = status;
    }

    public BorrowBook() {}
}
