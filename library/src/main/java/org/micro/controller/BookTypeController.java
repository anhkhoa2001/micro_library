package org.micro.controller;

import lombok.extern.slf4j.Slf4j;
import org.micro.dto.AuthorizationDTO;
import org.micro.dto.MessageContent;
import org.micro.dto.ResponseMessage;
import org.micro.model.Book;
import org.micro.model.BookType;
import org.micro.service.BookService;
import org.micro.service.BookTypeService;
import org.micro.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class BookTypeController {

    @Autowired
    private Validation validation;

    @Autowired
    private BookTypeService bookTypeService;

    @Autowired
    private BookService bookService;

    public ResponseMessage getAll(String requestPath, Map<String, String> headerParam) {
        ResponseMessage response = new ResponseMessage();
        AuthorizationDTO dto = validation.validateHeader(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập", null));

        } else {
            List<BookType> bookTypes = bookTypeService.getAll();
            if(bookTypes == null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Đã có lỗi xảy ra",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Đã có lỗi xảy ra", null));
            } else if(bookTypes.isEmpty()) {
                response = new ResponseMessage(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu",
                        new MessageContent(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu", null));

            } else {
                return new ResponseMessage(new MessageContent(bookTypes));
            }
        }

        return response;
    }

    public ResponseMessage addBookType(String requestPath, Map<String, String> headerParam, Map<String, Object> body) {
        ResponseMessage response = new ResponseMessage();
        AuthorizationDTO dto = validation.validateHeader(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập", null));

        } else {
            if(body == null || body.isEmpty()) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thêm dữ liệu vào body",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thêm dữ liệu vào body", null));
                return response;
            } else {
                String name = body.get("name").toString();
                if(name == null || name.isEmpty()) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Tên chuyên mục không được bỏ trống",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Tên chuyên mục không được bỏ trống", null));
                } else if(bookTypeService.getByName(name) != null) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Tên chuyên mục đã tồn tại",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Tên chuyên mục đã tồn tại", null));
                } else {
                    BookType bookType = new BookType(name);
                    bookType = bookTypeService.addBookType(bookType);
                    if(bookType != null) {
                        return new ResponseMessage(new MessageContent(bookType));
                    }
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thêm chuyên mục thất bại",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thêm chuyên mục thất bại", null));
                }
            }
        }

        return response;
    }

    public ResponseMessage edit(String requestPath, Map<String, String> headerParam, Map<String, Object> body) {
        ResponseMessage response = new ResponseMessage();
        AuthorizationDTO dto = validation.validateHeader(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập", null));

        } else {
            if(body == null || body.isEmpty()) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thêm dữ liệu vào body",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thêm dữ liệu vào body", null));
                return response;
            } else {
                Integer id = (Integer) body.get("id");
                String name = body.get("name").toString();
                if((name == null || name.isEmpty()) && (id == null || id <= 0)) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thông tin chuyên mục sách không hợp lệ",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thông tin chuyên mục sách không hợp lệ", null));
                    return response;
                } else if(bookTypeService.getByName(name) != null) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Tên chuyên mục sách đã tồn tại",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Tên chuyên mục sách đã tồn tại", null));
                    return response;
                }  else {
                    BookType bookType = bookTypeService.findById(id);
                    if(bookType == null) {
                        response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Chuyên mục sách cần sửa không tồn tại",
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), "Chuyên mục sách cần sửa không tồn tại", null));
                    } else {
                        bookType.setName(name);
                        bookType = bookTypeService.addBookType(bookType);
                        if(bookType != null) {
                            return new ResponseMessage(new MessageContent(bookType));
                        }
                        response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Sửa tác giả thất bại",
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), "Sửa tác giả thất bại", null));
                    }
                }
            }
        }

        return response;
    }

    public ResponseMessage delete(String requestPath, Map<String, String> headerParam,
                                  Map<String, String> urlParam) {
        ResponseMessage response = new ResponseMessage();
        AuthorizationDTO dto = validation.validateHeader(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập", null));

        } else {
            if(urlParam == null || urlParam.isEmpty()) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thêm dữ liệu vào url",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thêm dữ liệu vào url", null));
                return response;
            } else {
                Integer id = Integer.parseInt(urlParam.get("id"));
                List<Book> booksByTypeId = bookService.getBooksByBookType_Type_id(id);
                if(id == null || id <= 0) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "ID của tác giả không hợp lệ",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "ID của tác giả không hợp lệ", null));
                    return response;
                } else if(bookTypeService.findById(id) == null){
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Chuyên mục sách cần xóa không tồn tại",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Chuyên mục sách cần xóa không tồn tại", null));
                    return response;
                } else if(booksByTypeId != null && !booksByTypeId.isEmpty()) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Dữ liệu chuyên mục sách đang tồn tại trong sách nên không thể xóa",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Dữ liệu chuyên mục sách đang tồn tại trong sách nên không thể xóa", null));
                } else {
                    Boolean rs = bookTypeService.delete(id);
                    if(rs) {
                        return new ResponseMessage(new MessageContent("Xóa chuyên mục sách thành công"));
                    }
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Xóa tác giả thất bại",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Xóa tác giả thất bại", null));
                }
            }
        }

        return response;
    }
}
