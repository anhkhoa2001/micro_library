package org.micro.controller;

import lombok.extern.slf4j.Slf4j;
import org.micro.dto.*;
import org.micro.model.BookES;
import org.micro.service.BookESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class SearchController {

    @Autowired
    private BookESService bookESService;

    public ResponseMessage sync(String requestPath, Map<String, String> headerParam) {
        ResponseMessage response = new ResponseMessage();
        AuthorizationDTO dto = bookESService.validateHeader(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập", null));
        } else {
            List<BookDTO> bookDTOs = bookESService.getAllOnLibrary(headerParam);
            List<AuthorDTO> authorDTOs = bookESService.getAllAuthorOnLibrary(headerParam);
            List<BookTypeDTO> bookTypeDTOs = bookESService.getAllBookTypeOnLibrary(headerParam);
            if(bookDTOs == null || authorDTOs == null || bookTypeDTOs == null) {
                response = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lấy sách từ library service bị lỗi",
                        new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lấy sách từ library service bị lỗi", null));

            } else if(bookDTOs.isEmpty() || authorDTOs.isEmpty() || bookTypeDTOs.isEmpty()) {
                response = new ResponseMessage(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu để đồng bộ",
                        new MessageContent(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu để đồng bộ", null));

            } else {
                List<BookES> books = bookESService.synchronize(bookDTOs, authorDTOs, bookTypeDTOs);
                if(books == null || books.isEmpty()) {
                    response = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Đồng bộ dữ liệu thất bại",
                            new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Đồng bộ dữ liệu thất bại", null));
                } else {
                    return new ResponseMessage(new MessageContent(books));
                }
            }
        }

        return response;
    }

    public ResponseMessage findByName(String requestPath, Map<String, String> headerParam,
                                      Map<String, String> urlParam) {
        ResponseMessage response = new ResponseMessage();
        AuthorizationDTO dto = bookESService.validateHeader(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập", null));
        } else {
            if (urlParam == null || urlParam.isEmpty()) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thêm thông tin tìm kiếm vào url",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thêm thông tin tìm kiếm vào url", null));

            } else {
                String name = urlParam.get("name");
                if (name == null || name.isEmpty()) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Từ khóa tìm kiếm không được bỏ trống",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Từ khóa tìm kiếm không được bỏ trống", null));

                } else {
                    List<BookES> books = bookESService.findByName(name);
                    if (books == null || books.isEmpty()) {
                        response = new ResponseMessage(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu",
                                new MessageContent(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu", null));
                    } else {
                        return new ResponseMessage(new MessageContent(books));
                    }
                }
            }
        }

        return response;
    }
}
