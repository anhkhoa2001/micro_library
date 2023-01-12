package org.micro.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.micro.config.token.JwtTokenSetup;
import org.micro.dto.*;
import org.micro.model.Authority;
import org.micro.model.UserModel;
import org.micro.repository.AuthorityRepository;
import org.micro.service.AuthorityService;
import org.micro.service.UserService;
import org.micro.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenSetup jwt;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthorityService authorityService;

    public ResponseMessage login(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam) {
        ResponseMessage response = new ResponseMessage();
        if (bodyParam == null || bodyParam.isEmpty()) {
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thêm thông tin tài khoản vào body",
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thêm thông tin tài khoản vào body", null));
        }
        String username = (String) bodyParam.get("username");
        String password = (String) bodyParam.get("password");
        String message = new UserValidation().validateLogin(username, password);
        if (message != null) {
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), message,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), message, null));
        } else {
            UserModel user = userService.getUserByUsername(username);
            if (user == null) {
                message = "Tài khoản không tồn tại";
                return new ResponseMessage(HttpStatus.NOT_FOUND.value(), message,
                        new MessageContent(HttpStatus.NOT_FOUND.value(), message, null));
            } else {
                Authentication authentication = null;
                try {
                    authentication = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    username, password
                            )
                    );
                } catch (AuthenticationException e) {
                    message = "Tài khoản hoặc mật khẩu không đúng";
                    return new ResponseMessage(HttpStatus.BAD_REQUEST.value(), message,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), message, null));
                }

                UserDetailCustomize userDetail = (UserDetailCustomize) authentication.getPrincipal();
                if (!userDetail.getStatus()) {
                    message = "Tài khoản đã bị khóa";
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), message,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), message, null));
                } else {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String token = jwt.generateToken(userDetail);
                    response = new ResponseMessage(new MessageContent(new ResponseAuthentication(username, token, "Đang hoạt động!!!")));
                }
            }
        }

        return response;
    }

    public ResponseMessage register(String requestPath, Map<String, String> headerParam, Map<String, Object> bodyParam) {
        ResponseMessage response = new ResponseMessage();
        if (bodyParam == null || bodyParam.isEmpty()) {
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thêm thông tin tài khoản vào body",
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thêm thông tin tài khoản vào body", null));
        }
        String username = (String) bodyParam.get("username");
        String password = (String) bodyParam.get("password");
        Set<Integer> authories = new HashSet<>((List<Integer>) bodyParam.get("authories"));
        String message = new UserValidation().validateRegister(username, password, authories, authorityService);
        if (message != null) {
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), message,
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), message, null));
        } else {
            UserModel user = userService.getUserByUsername(username);
            if (user != null) {
                message = "Tài khoản đã tồn tại";
                return new ResponseMessage(HttpStatus.NOT_FOUND.value(), message,
                        new MessageContent(HttpStatus.NOT_FOUND.value(), message, null));
            } else {
                password = passwordEncoder.encode(password);
                UserDTO dto = new UserDTO(username, password, authories);
                user = userService.add(dto);
                response = new ResponseMessage(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(),
                        new MessageContent(user));
            }
        }

        return response;
    }

    public ResponseMessage addAuthority(String requestPath, Map<String, String> headerParam,
                                        Map<String, Object> bodyParam) {
        ResponseMessage response = new ResponseMessage();
        if (bodyParam == null || bodyParam.isEmpty()) {
            response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Thêm thông tin tài khoản vào body",
                    new MessageContent(HttpStatus.BAD_REQUEST.value(), "Thêm thông tin tài khoản vào body", null));
        } else {
            response = authorization(requestPath, headerParam);
            if(response.getStatus() == 200) {
                String name_authority = (String) bodyParam.get("name");

                String message = null;
                if(name_authority == null || name_authority.isEmpty()) {
                    message = "Không thể bỏ trống nội dung quyền";
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), message,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), message, null));

                } else if (authorityService.getByName(name_authority) != null) {
                    message = "Tên quyền đã tồn tại";
                    response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), message,
                            new MessageContent(HttpStatus.BAD_REQUEST.value(), message, null));
                } else {
                    Authority authority = authorityService.add(new Authority(name_authority));
                    if(authority != null) {
                        response = new ResponseMessage(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(),
                                new MessageContent(authority));
                    } else {
                        message = "Thêm tài khoản không thành công";
                        response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), message,
                                new MessageContent(HttpStatus.BAD_REQUEST.value(), message, null));
                    }
                }
            }
        }
        return response;
    }

    public ResponseMessage authorization(String requestPath, Map<String, String> headerParam) {
        ResponseMessage response = new ResponseMessage();
        String token = (String) headerParam.get("authorization");
        if(token != null && token.contains("Bearer ")) {
            token = token.replace("Bearer ", "");
            UserModel userModel = userService.getByToken(token);
            if(userModel != null ) {
                return new ResponseMessage(new MessageContent(userModel));
            }
        }
        response = new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập",
                new MessageContent(HttpStatus.UNAUTHORIZED.value(), "Vui lòng đăng nhập", null));

        return response;
    }
}

