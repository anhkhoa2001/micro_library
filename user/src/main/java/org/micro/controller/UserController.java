package org.micro.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.micro.dto.MessageContent;
import org.micro.dto.ResponseMessage;
import org.micro.model.UserModel;
import org.micro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private LoginController loginController;

    @Autowired
    private UserService userService;

    public ResponseMessage getById(String requestPath, Map<String, String> urlParam,
                                   Map<String, String> headerParam) {
        ResponseMessage response = new ResponseMessage();
        if (urlParam == null || urlParam.isEmpty()) {
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thêm thông tin ID vào url",
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thêm thông tin ID vào url", null));
        } else {
            response = loginController.authorization(requestPath, headerParam);
            if(response.getStatus() == 200) {
                String id = urlParam.get("id");
                if(NumberUtils.isNumber(id)) {
                    UserModel userModel = userService.getById(Integer.parseInt(id));

                    if(userModel != null) {
                        return new ResponseMessage(new MessageContent(userModel));
                    } else {
                        response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Đã có lỗi xảy ra",
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), "Đã có lỗi xảy ra", null));
                    }
                } else {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "ID không hợp lệ",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "ID không hợp lệ", null));
                }
            }
        }

        return response;
    }

    public ResponseMessage getAllUser(String requestPath, Map<String, String> headerParam,
                                      Map<String, Object> bodyParam) {
        ResponseMessage response = new ResponseMessage();
        response = loginController.authorization(requestPath, headerParam);
        if(response.getStatus() == 200) {
            if(bodyParam == null || bodyParam.isEmpty()) {
                List<UserModel> userModels = userService.getAll();
                if(userModels == null) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Đã có lỗi xảy ra",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Đã có lỗi xảy ra", null));
                    return response;
                } else if(userModels.isEmpty()) {
                    response = new ResponseMessage(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu",
                            new MessageContent(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu", null));
                    return response;
                } else {
                    return new ResponseMessage(new MessageContent(userModels));
                }
            } else {
                Object obj = bodyParam.get("ids");
                if(obj == null || obj.getClass() != ArrayList.class) {
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thông tin body không hợp lệ",
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thông tin body không hợp lệ", null));

                } else {
                    List<Integer> ids = (List<Integer>) obj;
                    List<UserModel> users = userService.getUsersById(ids);
                    if(users == null) {
                        response = new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lấy dữ liệu bị lỗi",
                                new MessageContent(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lấy dữ liệu bị lỗi", null));
                    } else if(users.isEmpty()) {
                        response = new ResponseMessage(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu",
                                new MessageContent(HttpStatus.NO_CONTENT.value(), "Không có dữ liệu", null));
                    } else {
                        return new ResponseMessage(new MessageContent(users));
                    }
                }
            }
        }
        return response;
    }
}
