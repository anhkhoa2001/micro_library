package org.micro.controller;

import lombok.extern.slf4j.Slf4j;
import org.micro.dto.AuthorizationDTO;
import org.micro.dto.MessageContent;
import org.micro.dto.ResponseMessage;
import org.micro.model.Author;
import org.micro.model.Book;
import org.micro.model.BookType;
import org.micro.service.AuthorService;
import org.micro.service.BookService;
import org.micro.service.BookTypeService;
import org.micro.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class AuthorController {

    @Autowired
    private Validation validation;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookService bookService;

    public ResponseMessage getAll(String requestPath, Map<String, String> headerParam,
                                  Map<String, String> urlParam) {
        ResponseMessage response = new ResponseMessage();
        AuthorizationDTO dto = validation.validateHeader(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập", null));

        } else {
            List<Author> authors = new ArrayList<>();
            if(urlParam == null || urlParam.isEmpty()) {
                authors = authorService.getAll();
            } else {
                try {
                    Integer limit = Integer.parseInt(urlParam.get("lim"));
                    Integer offset = Integer.parseInt(urlParam.get("off"));

                    authors = authorService.getAuthorsByPage(limit, offset);
                } catch (Exception e) {
                    e.printStackTrace();
                    authors = null;
                }
            }
            if(authors == null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Đã có lỗi xảy ra",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Đã có lỗi xảy ra", null));
            } else if(authors.isEmpty()) {
                response = new ResponseMessage(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu",
                        new MessageContent(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu", null));

            } else {
                return new ResponseMessage(new MessageContent(authors));
            }
        }

        return response;
    }

    public ResponseMessage addAuthor(String requestPath, Map<String, String> headerParam, Map<String, Object> body) {
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
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Tên tác giả không được bỏ trống",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Tên tác giả không được bỏ trống", null));
                } else if(authorService.getByName(name) != null) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Tên tác giả đã tồn tại",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Tên tác giả đã tồn tại", null));
                } else {
                    Author author = new Author(name);
                    author = authorService.addAuthor(author);
                    if(author != null) {
                        return new ResponseMessage(new MessageContent(author));
                    }
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thêm tác giả thất bại",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thêm tác giả thất bại", null));
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
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thông tin tác giả không hợp lệ",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thông tin tác giả không hợp lệ", null));
                    return response;
                } else {
                    Author author = authorService.findById(id);
                    if(author == null) {
                        response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Tác giả cần sửa không tồn tại",
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), "Tác giả cần sửa không tồn tại", null));
                    } else {
                        author.setName(name);
                        author = authorService.addAuthor(author);
                        if(author != null) {
                            return new ResponseMessage(new MessageContent(author));
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
                List<Book> booksByAuthorId = bookService.getBooksByAuthor_Id(id);
                if(id == null || id <= 0) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "ID của tác giả không hợp lệ",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "ID của tác giả không hợp lệ", null));
                } else if(authorService.findById(id) == null){
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Tác giả cần xóa không tồn tại",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Tác giả cần xóa không tồn tại", null));
                } else if(booksByAuthorId != null && !booksByAuthorId.isEmpty()) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Dữ liệu tác giả đang tồn tại trong sách nên không thể xóa",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Dữ liệu tác giả đang tồn tại trong sách nên không thể xóa", null));
                } else {
                    Boolean rs = authorService.delete(id);
                    if(rs) {
                        return new ResponseMessage(new MessageContent("Xóa tác giả thành công"));
                    }
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Xóa tác giả thất bại",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Xóa tác giả thất bại", null));
                }
            }
        }

        return response;
    }
}
