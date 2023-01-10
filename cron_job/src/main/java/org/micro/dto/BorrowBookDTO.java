package org.micro.dto;

import lombok.Data;
import org.micro.enums.StatusBorrowBook;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Data
public class BorrowBookDTO {

    private Integer id;
    private Integer user_id;
    private Integer book_id;
    private Boolean is_borrow;
    private Date start_date;
    private Date due_date;
    private Date return_date;
    private StatusBorrowBook status;

    public BorrowBookDTO(Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            if (map.containsKey("id")) {
                this.id = (Integer) map.get("id");
            }
            if (map.containsKey("user_id")) {
                this.user_id = (Integer) map.get("user_id");
            }
            if (map.containsKey("book_id")) {
                this.book_id = (Integer) map.get("book_id");
            }
            if (map.containsKey("is_borrow")) {
                this.is_borrow = (Boolean) map.get("is_borrow");
            }
            if (map.containsKey("start_date")) {
                this.start_date = toDate(map.get("start_date").toString());
            }
            if (map.containsKey("due_date")) {
                this.due_date = toDate(map.get("due_date").toString());
            }
            if (map.containsKey("return_date") && map.get("return_date") != null) {
                this.return_date = toDate(map.get("return_date").toString());
            }
            if (map.containsKey("status")) {
                this.status = StatusBorrowBook.valueOf(map.get("status").toString());
            }
        }
    }

    public Date toDate(String source) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.parse(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
