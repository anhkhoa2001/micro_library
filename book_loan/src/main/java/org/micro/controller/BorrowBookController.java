package org.micro.controller;

import lombok.extern.slf4j.Slf4j;
import org.micro.dto.AuthorizationDTO;
import org.micro.dto.BookDTO;
import org.micro.dto.MessageContent;
import org.micro.dto.ResponseMessage;
import org.micro.enums.StatusBorrowBook;
import org.micro.model.BorrowBook;
import org.micro.service.BorrowBookService;
import org.micro.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class BorrowBookController {

    @Autowired
    private Validation validation;

    @Autowired
    private BorrowBookService borrowBookService;

    public ResponseMessage borrowBook(String requestPath, Map<String, String> headerParam,
                                      Map<String, Object> bodyMap) {
        ResponseMessage response = new ResponseMessage();
        AuthorizationDTO dto = validation.validateHeader(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập", null));

        } else {
            if(bodyMap == null || bodyMap.isEmpty()) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thêm thông tin mượn sách vào body",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thêm thông tin mượn sách vào body", null));
                return response;
            } else {
                Integer user_id = (Integer) bodyMap.get("user_id");
                Integer book_id = (Integer) bodyMap.get("book_id");
                StatusBorrowBook status = StatusBorrowBook.WAIT;
                Object due_date = bodyMap.get("due_date");
                Date start_date = new Date();
                Boolean is_borrow = true;

                BorrowBook borrowBook = new BorrowBook(user_id, book_id, is_borrow, start_date, null, status);
                String message = validation.validateInfoBorrow(borrowBook, due_date);
                if(message != null) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), message,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), message, null));
                    return response;
                } else {
                    BookDTO bookDTO = validation.validateBook(headerParam, book_id);
                    if(Integer.parseInt(dto.getId()) != user_id) {
                        response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "ID sinh viên mượn sách không hợp lệ",
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), "ID sinh viên mượn sách không hợp lệ", null));
                    } else if(bookDTO == null) {
                        response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "ID sách mượn không tồn tại",
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), "ID sách mượn không tồn tại", null));
                    } else {
                        borrowBook = borrowBookService.add(borrowBook);
                        if(borrowBook != null) {
                            return new ResponseMessage(new MessageContent(borrowBook));
                        }
                        response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Mượn sách thất bại",
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), "Mượn sách thất bại", null));
                    }
                }
            }
        }

        return response;
    }

    public ResponseMessage returnBook(String requestPath, Map<String, String> headerParam,
                                      Map<String, Object> bodyMap) {
        ResponseMessage response = new ResponseMessage();
        AuthorizationDTO dto = validation.validateHeader(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập", null));

        } else {
            if(bodyMap == null || bodyMap.isEmpty()) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thêm thông tin mượn sách vào body",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thêm thông tin mượn sách vào body", null));
            } else {
                Integer id = (Integer) bodyMap.get("id");
                if(id == null || id <= 0) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thông tin mượn sách không tồn tại",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thông tin mượn sách không tồn tại", null));
                } else {
                    BorrowBook borrowBook = borrowBookService.findById(id);
                    if (borrowBook != null) {
                        Date now = new Date();
                        borrowBook.setStatus(now.compareTo(borrowBook.getDue_date()) > 0 ?
                                StatusBorrowBook.EXPRIED : StatusBorrowBook.DONE);
                        borrowBook = borrowBookService.add(borrowBook);
                        borrowBook.setIs_borrow(false);
                        borrowBook.setReturn_date(now);
                        borrowBook.setId(null);
                        borrowBook = borrowBookService.add(borrowBook);
                        if(borrowBook != null) {
                            return new ResponseMessage(new MessageContent(borrowBook));
                        }
                    }
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Trả sách thất bại",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Trả sách thất bại", null));
                }
            }
        }

        return response;
    }

    public ResponseMessage getAll(String requestPath, Map<String, String> headerParam) {
        ResponseMessage response = new ResponseMessage();
        AuthorizationDTO dto = validation.validateHeader(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập", null));

        } else {
            List<BorrowBook> borrowBooks = borrowBookService.getAll();
            if (borrowBooks == null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Lấy log mượn sách thất bại",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Lấy log mượn sách thất bại", null));

            } else if(borrowBooks.isEmpty()) {
                response = new ResponseMessage(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu",
                        new MessageContent(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu", null));

            } else {
                return new ResponseMessage(new MessageContent(borrowBooks));
            }

        }

        return response;
    }
}
