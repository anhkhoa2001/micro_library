package org.micro.controller;

import lombok.extern.slf4j.Slf4j;
import org.micro.dto.AuthorizationDTO;
import org.micro.dto.BookDTO;
import org.micro.dto.MessageContent;
import org.micro.dto.ResponseMessage;
import org.micro.model.Author;
import org.micro.model.Book;
import org.micro.mq.RabbitMQClient;
import org.micro.service.AuthorService;
import org.micro.service.BookService;
import org.micro.service.BookTypeService;
import org.micro.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@Slf4j
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private Validation validation;
    @Autowired
    private BookTypeService bookTypeService;
    @Autowired
    private AuthorService authorService;

    public ResponseMessage getAll(String requestPath, Map<String, String> headerParam) {
        ResponseMessage response = new ResponseMessage();
        AuthorizationDTO dto = validation.validateHeader(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập", null));

        } else {
            List<Book> books = bookService.getAll();
            if(books == null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Đã có lỗi xảy ra",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Đã có lỗi xảy ra", null));
            } else if(books.isEmpty()) {
                response = new ResponseMessage(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu",
                        new MessageContent(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu", null));

            } else {
                return new ResponseMessage(new MessageContent(books));
            }
        }

        return response;
    }

    public ResponseMessage addBook(String requestPath, Map<String, String> headerParam, Map<String, Object> body) {
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
                String content = body.get("content").toString();
                Integer author_id = (Integer) body.get("author_id");
                Integer type_id = (Integer) body.get("type_id");
                BookDTO bookDTO = new BookDTO(name, content, author_id, type_id);
                String message = validation.validateBook(bookDTO);
                if(message != null) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), message,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), message, null));
                } else {
                    Book book = bookService.addBook(bookDTO);
                    if(book != null) {
                        return new ResponseMessage(new MessageContent(book));
                    }
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thêm sách thất bại",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thêm sách thất bại", null));
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
                String content = body.get("content").toString();
                Integer author_id = (Integer) body.get("author_id");
                Integer type_id = (Integer) body.get("type_id");
                BookDTO bookDTO = new BookDTO(name, content, author_id, type_id);
                if(id == null || id <= 0) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "ID của sách không hợp lệ",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "ID của sách không hợp lệ", null));
                    return response;
                } else {
                    Book book = bookService.findById(id);
                    if(book == null) {
                        response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Sách cần sửa không tồn tại",
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), "Sách cần sửa không tồn tại", null));
                        return response;
                    } else {
                        bookDTO.setId(id);
                        String message = validation.validateBook(bookDTO);
                        if(message != null) {
                            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), message,
                                    new MessageContent(HttpStatus.BAD_REQUEST.value(), message, null));
                            return response;
                        } else {
                            book.setContent(bookDTO.getContent());
                            book.setName(bookDTO.getName());
                            book.setAuthor(authorService.findById(bookDTO.getAuthor_id()));
                            book.setBookType(bookTypeService.findById(bookDTO.getType_id()));
                            book = bookService.update(book);
                            if(book != null) {
                                return new ResponseMessage(new MessageContent(book));
                            }

                            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Sửa sách thất bại",
                                    new MessageContent(HttpStatus.BAD_REQUEST.value(), "Sửa sách thất bại", null));
                        }
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
                if(id == null || id <= 0) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "ID của sách không hợp lệ",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "ID của sách không hợp lệ", null));
                    return response;
                } else if(bookService.findById(id) == null){
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Sách cần xóa không tồn tại",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Sách cần xóa không tồn tại", null));
                    return response;
                }else {
                    Boolean rs = bookService.delete(id);
                    if(rs) {
                        return new ResponseMessage(new MessageContent("Xóa sách thành công"));
                    }
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Xóa sách thất bại",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Xóa sách thất bại", null));
                }
            }
        }

        return response;
    }
}
