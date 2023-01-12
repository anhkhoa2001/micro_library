package org.micro.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.micro.dto.*;
import org.micro.mq.RabbitMQClient;
import org.micro.mq.RabbitMQProperties;
import org.micro.service.AuthorService;
import org.micro.service.BookService;
import org.micro.service.BookTypeService;
import org.micro.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class Validation {
    @Autowired
    private RabbitMQClient client;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookTypeService bookTypeService;

    @Value("${micro.url.authorization}")
    private String USER_URL_AUTHORICATION;

    @Value("${micro.url.method}")
    private String USER_METHOD_AUTHORICATION;

    public AuthorizationDTO validateHeader(Map<String, String> headerParam) {
        //Authen -> call rpc authen headerMap
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod(USER_METHOD_AUTHORICATION);
        userRpcRequest.setRequestPath(USER_URL_AUTHORICATION);
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

    public String validateBook(BookDTO bookDTO) {
        String name = bookDTO.getName();
        String content = bookDTO.getContent();
        Integer author_id = bookDTO.getAuthor_id();
        Integer type_id = bookDTO.getType_id();

        if(name == null || name.isEmpty()) {
            return "Tên sách không được để trống";
        } else if(bookService.getByName(name) != null) {
            return "Tên sách đã tồn tại";
        }

        if(content == null || content.isEmpty()) {
            return "Nội dung sách không được để trống";
        }

        if(author_id == null || author_id <= 0 || authorService.findById(author_id) == null) {
            return "ID của tác giả không hợp lệ";
        }
        System.out.println(authorService.findById(author_id));

        if(type_id == null || type_id <= 0 || bookTypeService.findById(type_id) == null) {
            return "ID của chuyên mục sách không hợp lệ";
        }

        return null;
    }
}
