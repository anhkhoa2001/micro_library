package org.micro.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.micro.dto.*;
import org.micro.model.BorrowBook;
import org.micro.mq.RabbitMQClient;
import org.micro.mq.RabbitMQProperties;
import org.micro.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class Validation {
    @Autowired
    private RabbitMQClient client;

    private final String USER_URL_AUTHENTICATION = "/api/user/authorization";
    private final String LIBRARY_URL_GET_BY_ID = "/api/library/book/get-by-id";

    public AuthorizationDTO validateHeader(Map<String, String> headerParam) {
        //Authen -> call rpc authen headerMap
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("POST");
        userRpcRequest.setRequestPath(USER_URL_AUTHENTICATION);
        userRpcRequest.setBodyParam(null);
        userRpcRequest.setUrlParam(null);
        userRpcRequest.setHeaderParam(headerParam);
        String result = client.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, userRpcRequest.toJsonString());
        log.info("authenToken - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage response = null;
            try {
                response = mapper.readValue(result, ResponseMessage.class);
            } catch (JsonProcessingException ex) {
                log.info("Lỗi parse json khi gọi user service verify: " + ex.toString());
                return null;
            }

            if (response != null && response.getStatus() == HttpStatus.OK.value()) {
                try {
                    //Process
                    MessageContent content = response.getData();
                    Object data = content.getData();
                    if (data != null) {
                        AuthorizationDTO dto = null;
                        if (data.getClass() == AuthorizationDTO.class) {
                            dto = (AuthorizationDTO) data;
                        } else if (data.getClass() == LinkedHashMap.class) {
                            dto = new AuthorizationDTO((Map<String, Object>) data);
                        }
                        if (dto != null && !StringUtil.isNullOrEmpty(dto.getId())) {
                            return dto;
                        }
                    }
                } catch (Exception ex) {
                    log.info("Lỗi giải mã AuthorizationDTO khi gọi user service verify: " + ex.toString());
                    return null;
                }
            } else {
                //Forbidden
                return null;
            }
        } else {
            //Forbidden
            return null;
        }
        return null;
    }

    public BookDTO validateBook(Map<String, String> headerParam, Integer book_id) {
        //Authen -> call rpc authen headerMap
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("GET");
        userRpcRequest.setRequestPath(LIBRARY_URL_GET_BY_ID);
        userRpcRequest.setBodyParam(null);
        userRpcRequest.setHeaderParam(headerParam);
        Map<String, String> urlParam = new HashMap<>();
        urlParam.put("id", book_id.toString());
        userRpcRequest.setUrlParam(urlParam);
        String result = client.callRpcService(RabbitMQProperties.LIBRARY_RPC_EXCHANGE,
                RabbitMQProperties.LIBRARY_RPC_QUEUE, RabbitMQProperties.LIBRARY_RPC_KEY, userRpcRequest.toJsonString());
        log.info("book - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage response = null;
            try {
                response = mapper.readValue(result, ResponseMessage.class);
            } catch (JsonProcessingException ex) {
                log.info("Lỗi parse json khi gọi library service verify: " + ex.toString());
                return null;
            }

            if (response != null && response.getStatus() == HttpStatus.OK.value()) {
                try {
                    //Process
                    MessageContent content = response.getData();
                    Object data = content.getData();
                    if (data != null) {
                        BookDTO dto = null;
                        if (data.getClass() == BookDTO.class) {
                            dto = (BookDTO) data;
                        } else if (data.getClass() == LinkedHashMap.class) {
                            dto = new BookDTO((Map<String, Object>) data);
                        }
                        if (dto != null && dto.getId() >= 0) {
                            return dto;
                        }
                    }
                } catch (Exception ex) {
                    log.info("Lỗi giải mã BookDTO khi gọi library service verify: " + ex.toString());
                    return null;
                }
            } else {
                //Forbidden
                return null;
            }
        } else {
            //Forbidden
            return null;
        }
        return null;
    }

    public String validateInfoBorrow(BorrowBook borrowBook, Object due_date) {
        if(borrowBook.getBook_id() == null || borrowBook.getBook_id() <= 0) {
            return "Thông tin ID sách không hợp lệ";
        }
        if(borrowBook.getUser_id() == null || borrowBook.getUser_id() <= 0) {
            return "Thông tin ID sinh viên không hợp lệ";
        }
        Boolean isno = true;
        try {
            String expiredString = due_date.toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date due = sdf.parse(expiredString);

            if(borrowBook.getStart_date().compareTo(due) > 0) {
                isno = false;
            } else {
                isno = true;
                borrowBook.setDue_date(due);
            }
        } catch (Exception e) {
            isno = false;
            e.printStackTrace();
        }
        if(!isno) {
            return "Ngày hết hạn không hợp lệ";
        }

        return null;
    }
}
