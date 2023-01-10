package org.micro.enums;

public enum StatusBorrowBook {

    DONE(1, "Đã trả sách"),
    WAIT(2, "Chưa trả sách trong thời gian mượn"),
    EXPRIED(3, "Đã trả sách nhưng bị quá hạn"),
    OVERDUE(4, "Quá hạn");

    private int code;
    private String description;

    StatusBorrowBook(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
