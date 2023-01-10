package org.micro.controller;

import lombok.extern.slf4j.Slf4j;
import org.micro.dto.AuthorizationDTO;
import org.micro.dto.MessageContent;
import org.micro.dto.ResponseMessage;
import org.micro.service.BorrowBookService;
import org.micro.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Slf4j
@RestController
public class StatisticalBorrowController {

    @Autowired
    private Validation validation;

    @Autowired
    private BorrowBookService borrowBookService;


    //lấy số lượng sách mượn theo khoảng thời gian
    public ResponseMessage getCountByTime(String requestPath, Map<String, String> headerParam,
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
                try {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date start = dateFormat.parse((String) bodyMap.get("start"));
                    Date end = dateFormat.parse((String) bodyMap.get("end"));

                    if(start.compareTo(end) <= 0) {
                        long count = borrowBookService.countByTimer(start, end);
                        count = count == -2001 ? 0 : count;

                        return new ResponseMessage(new MessageContent(count));
                    } else {
                        response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thời gian trong bắt đầu phải nhỏ hơn thời gian kết thúc",
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thời gian trong bắt đầu phải nhỏ hơn thời gian kết thúc", null));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Ngày trong body không hợp lệ",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Ngày trong body không hợp lệ", null));
            }
        }

        return response;
    }

    //lấy id của sách được mượn nhiều nhất
    public ResponseMessage getBookIdByTime(String requestPath, Map<String, String> headerParam,
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
                try {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date start = dateFormat.parse((String) bodyMap.get("start"));
                    Date end = dateFormat.parse((String) bodyMap.get("end"));

                    if(start.compareTo(end) <= 0) {
                        long count = borrowBookService.getBookIdByTimer(start, end);
                        count = count == -2001 ? 0 : count;

                        return new ResponseMessage(new MessageContent(count));
                    } else {
                        response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thời gian trong bắt đầu phải nhỏ hơn thời gian kết thúc",
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thời gian trong bắt đầu phải nhỏ hơn thời gian kết thúc", null));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Ngày trong body không hợp lệ",
                        new MessageContent(HttpStatus.BAD_REQUEST.value(), "Ngày trong body không hợp lệ", null));

            }
        }

        return response;
    }
}
