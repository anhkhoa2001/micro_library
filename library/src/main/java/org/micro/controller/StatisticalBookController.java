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
public class StatisticalBookController {

    @Autowired
    private Validation validation;

    @Autowired
    private StatisticalService statisticalService;

    public ResponseMessage statisAuthor(String requestPath, Map<String, String> headerParam) {
        ResponseMessage response = new ResponseMessage();
        AuthorizationDTO dto = validation.validateHeader(headerParam);
        if(dto == null) {
            response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập",
                    new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập", null));

        } else {
            List<StatisticalByAuthor> result = statisticalService.getAllByAuthor();
            if(result == null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thống kê bị lỗi",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thống kê bị lỗi", null));

            } else if(result.isEmpty()) {
                response = new ResponseMessage(HttpStatus.NO_CONTENT.value(), "Vui lòng đăng nhập",
                        new MessageContent(HttpStatus.NO_CONTENT.value(), "Vui lòng đăng nhập", null));
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
            List<StatisticalByCharacter> result = statisticalService.getAllByFirstCharacter();
            if(result == null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thống kê bị lỗi",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thống kê bị lỗi", null));

            } else if(result.isEmpty()) {
                response = new ResponseMessage(HttpStatus.NO_CONTENT.value(), "Vui lòng đăng nhập",
                        new MessageContent(HttpStatus.NO_CONTENT.value(), "Vui lòng đăng nhập", null));
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
            List<StatisticalByType> result = statisticalService.getAllByType();
            if(result == null) {
                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thống kê bị lỗi",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thống kê bị lỗi", null));

            } else if(result.isEmpty()) {
                response = new ResponseMessage(HttpStatus.NO_CONTENT.value(), "Vui lòng đăng nhập",
                        new MessageContent(HttpStatus.NO_CONTENT.value(), "Vui lòng đăng nhập", null));
            } else {
                return new ResponseMessage(new MessageContent(result));
            }
        }

        return response;
    }
}
