package org.micro.repository;

import org.micro.model.BorrowBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BorrowBookRepository extends JpaRepository<BorrowBook, Integer> {

    @Query(value = "select count(m) from mbl_borrow_book m where" +
            " m.start_date >= :start and m.start_date <= :end and m.is_borrow = true", nativeQuery = true)
    long countByTimer(@Param("start") Date start, @Param("end") Date end);

    @Query(value = "select pp.book_id from (select count(p) as count, p.book_id  " +
            " from mbl_borrow_book p where start_date >= :start and start_date <= :end and is_borrow = true" +
            " group by book_id) as pp order by pp.count desc limit 1", nativeQuery = true)
    long getBookIdByTimer(@Param("start") Date start, @Param("end") Date end);

    @Query(value = "select mbb.* from mbl_borrow_book mbb where mbb.is_borrow = true " +
                        "and mbb.status not in (0, 2) and mbb.due_date <= now();", nativeQuery = true)
    List<BorrowBook> findAllByIs_borrow();
}
