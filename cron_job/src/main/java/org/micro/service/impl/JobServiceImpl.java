package org.micro.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.micro.dto.*;
import org.micro.mq.RabbitMQClient;
import org.micro.mq.RabbitMQProperties;
import org.micro.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class JobServiceImpl implements JobService {
    private final String USER_URL_LOGIN = "/api/user/login";
    private final String BOOK_LOAN_URL_GET_ALL = "/api/book_loan/borrow_book";
    private final String USER_URL_GET_ALL = "/api/user/get-by-id";
    private final String LIBRARY_URL_GET_BY_ID = "/api/library/book/get-by-id";
    @Autowired
    private RabbitMQClient client;

    @Value("${micro.app.username}")
    private String username;

    @Value("${micro.app.password}")
    private String password;

    @Override
    public List<BorrowBookDTO> getAllBorrow() {
        String token = autoAuthorization();
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("GET");
        userRpcRequest.setRequestPath(BOOK_LOAN_URL_GET_ALL);
        userRpcRequest.setBodyParam(null);
        userRpcRequest.setUrlParam(null);
        Map<String, String> headerParam = new HashMap<>();
        headerParam.put("authorization", "Bearer " + token);
        userRpcRequest.setHeaderParam(headerParam);
        String result = client.callRpcService(RabbitMQProperties.BOOK_LOAN_RPC_EXCHANGE,
                RabbitMQProperties.BOOK_LOAN_RPC_QUEUE, RabbitMQProperties.BOOK_LOAN_RPC_KEY, userRpcRequest.toJsonString());
        log.info("book loan get all - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage response = null;
            try {
                response = mapper.readValue(result, ResponseMessage.class);
            } catch (JsonProcessingException ex) {
                log.info("Lỗi parse json khi gọi book loan service verify: " + ex.toString());
                return null;
            }

            if (response != null && response.getStatus() == HttpStatus.OK.value()) {
                try {
                    //Process
                    MessageContent content = response.getData();
                    Object data = content.getData();
                    if (data != null) {
                        List<BorrowBookDTO> dtos = null;
                        if (data.getClass() == ArrayList.class) {
                            dtos = new ArrayList<>();
                            List<LinkedHashMap> list = (List<LinkedHashMap>) data;
                            for(LinkedHashMap l:list) {
                                dtos.add(new BorrowBookDTO(l));
                            }
                        }
                        if (dtos != null) {
                            return dtos;
                        }
                    }
                } catch (Exception ex) {
                    log.info("Lỗi giải mã getAllBorrow khi gọi book loan service verify: " + ex.toString());
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
        return null;
    }

    @Override
    public UserDTO getUserById(Integer id) {
        String token = autoAuthorization();
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("GET");
        userRpcRequest.setRequestPath(USER_URL_GET_ALL);
        userRpcRequest.setUrlParam(null);
        Map<String, String> headerParam = new HashMap<>();
        Map<String, String> urlParam = new HashMap<>();
        urlParam.put("id", id.toString());
        headerParam.put("authorization", "Bearer " + token);
        userRpcRequest.setHeaderParam(headerParam);
        userRpcRequest.setUrlParam(urlParam);
        String result = client.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, userRpcRequest.toJsonString());
        log.info(" get all user by ids - result: " + result);
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
                        UserDTO dto = null;
                        if(data.getClass() == UserDTO.class) {
                            dto = (UserDTO) data;
                        } else if(data.getClass() == LinkedHashMap.class) {
                            dto = new UserDTO((LinkedHashMap) data);
                        }
                        if (dto != null) {
                            return dto;
                        }
                    }
                } catch (Exception ex) {
                    log.info("Lỗi giải mã getAllUserById khi gọi user service verify: " + ex.toString());
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
        return null;
    }

    @Override
    public BookDTO getBookById(Integer id) {
        String token = autoAuthorization();
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("GET");
        userRpcRequest.setRequestPath(LIBRARY_URL_GET_BY_ID);
        userRpcRequest.setUrlParam(null);
        Map<String, String> headerParam = new HashMap<>();
        Map<String, String> urlParam = new HashMap<>();
        urlParam.put("id", id.toString());
        headerParam.put("authorization", "Bearer " + token);
        userRpcRequest.setHeaderParam(headerParam);
        userRpcRequest.setUrlParam(urlParam);
        String result = client.callRpcService(RabbitMQProperties.LIBRARY_RPC_EXCHANGE,
                RabbitMQProperties.LIBRARY_RPC_QUEUE, RabbitMQProperties.LIBRARY_RPC_KEY, userRpcRequest.toJsonString());
        log.info(" get book by id - result: " + result);
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
                        if(data.getClass() == BookDTO.class) {
                            dto = (BookDTO) data;
                        } else if(data.getClass() == LinkedHashMap.class) {
                            dto = new BookDTO((LinkedHashMap) data);
                        }
                        if (dto != null) {
                            return dto;
                        }
                    }
                } catch (Exception ex) {
                    log.info("Lỗi giải mã getAllBookByIds khi gọi user service verify: " + ex.toString());
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
        return null;
    }

    public String autoAuthorization() {
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("POST");
        userRpcRequest.setRequestPath(USER_URL_LOGIN);
        userRpcRequest.setUrlParam(null);
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("username", username);
        bodyParam.put("password", password);
        userRpcRequest.setBodyParam(bodyParam);
        String result = client.callRpcService(RabbitMQProperties.USER_RPC_EXCHANGE,
                RabbitMQProperties.USER_RPC_QUEUE, RabbitMQProperties.USER_RPC_KEY, userRpcRequest.toJsonString());
        log.info("authenToken - result: " + result);
        if (result != null) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseMessage response = null;
            try {
                response = mapper.readValue(result, ResponseMessage.class);
                if(response.getData().getData() != null) {
                    LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) response.getData().getData();

                    return (String) map.get("username");
                }
            } catch (JsonProcessingException ex) {
                log.info("Lỗi parse json khi gọi user service verify: " + ex.toString());
                return null;
            }
        } else {
            return null;
        }
        return null;
    }


}
