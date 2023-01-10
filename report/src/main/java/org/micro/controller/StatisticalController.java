package org.micro.controller;

import lombok.extern.slf4j.Slf4j;
import org.micro.dto.*;
import org.micro.service.StatisticalService;
import org.micro.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class StatisticalController {

    @Autowired
    private StatisticalService statisticalService;

    @Autowired
    private Validation validation;

    public ResponseMessage statisAuthor(String requestPath, Map<String, String> headerParam) {
        ResponseMessage response = new ResponseMessage();
        AuthorizationDTO dto = validation.validateHeader(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập", null));

        } else {
            List<StatisticalByAuthor> result = statisticalService.getAllByAuthor(headerParam);
            if(result == null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thống kê bị lỗi",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thống kê bị lỗi", null));

            } else if(result.isEmpty()) {
                response = new ResponseMessage(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu",
                        new MessageContent(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu", null));
            } else {
                return new ResponseMessage(new MessageContent(result));
            }
        }

        return response;
    }

    public ResponseMessage statisType(String requestPath, Map<String, String> headerParam) {
        ResponseMessage response = new ResponseMessage();
        AuthorizationDTO dto = validation.validateHeader(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập", null));

        } else {
            List<StatisticalByType> result = statisticalService.getAllByType(headerParam);
            if(result == null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thống kê bị lỗi",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thống kê bị lỗi", null));

            } else if(result.isEmpty()) {
                response = new ResponseMessage(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu",
                        new MessageContent(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu", null));
            } else {
                return new ResponseMessage(new MessageContent(result));
            }
        }

        return response;
    }

    public ResponseMessage statisCharacter(String requestPath, Map<String, String> headerParam) {
        ResponseMessage response = new ResponseMessage();
        AuthorizationDTO dto = validation.validateHeader(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập", null));

        } else {
            List<StatisticalByCharacter> result = statisticalService.getAllByFirstCharacter(headerParam);
            if(result == null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thống kê bị lỗi",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thống kê bị lỗi", null));

            } else if(result.isEmpty()) {
                response = new ResponseMessage(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu",
                        new MessageContent(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu", null));
            } else {
                return new ResponseMessage(new MessageContent(result));
            }
        }

        return response;
    }

    public ResponseMessage statisCount(String requestPath, Map<String, String> headerParam,
                                           Map<String, Object> body) {
        ResponseMessage response = new ResponseMessage();
        AuthorizationDTO dto = validation.validateHeader(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập", null));

        } else {
            if(body == null || body.isEmpty()) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thêm dữ liệu vào body",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thêm dữ liệu vào body", null));

            } else {
                Long l = statisticalService.countByTimer(headerParam, body);
                if(l == null) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thống kê bị lỗi",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thống kê bị lỗi", null));

                } else if(l <= 0) {
                    response = new ResponseMessage(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu",
                            new MessageContent(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu", null));
                } else {
                    return new ResponseMessage(new MessageContent(l));
                }
            }
        }

        return response;
    }

    public ResponseMessage statisBookId(String requestPath, Map<String, String> headerParam,
                                       Map<String, Object> body) {
        ResponseMessage response = new ResponseMessage();
        AuthorizationDTO dto = validation.validateHeader(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập", null));

        } else {
            if(body == null || body.isEmpty()) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thêm dữ liệu vào body",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thêm dữ liệu vào body", null));

            } else {
                Integer id = statisticalService.getBookIdByTimer(headerParam, body);
                if(id == null) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thống kê bị lỗi",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thống kê bị lỗi", null));
                } else {
                    Object book = statisticalService.getBookById(id, headerParam);
                    if(book != null) {
                        return new ResponseMessage(new MessageContent(book));
                    }
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thống kê bị lỗi",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thống kê bị lỗi", null));
                }
            }
        }

        return response;
    }
}

