package org.micro.service.impl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.micro.dto.*;
import org.micro.model.AuthorES;
import org.micro.model.BookES;
import org.micro.model.BookTypeES;
import org.micro.mq.RabbitMQClient;
import org.micro.mq.RabbitMQProperties;
import org.micro.repository.AuthorESRepository;
import org.micro.repository.BookESRepository;
import org.micro.repository.BookTypeESRepository;
import org.micro.service.BookESService;
import org.micro.service.converter.BookESConverter;
import org.micro.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookESServiceImpl implements BookESService {

    private final String USER_URL_AUTHENTICATION = "/api/user/authentication";
    private final String LIBRARY_URL_GET_ALL_BOOK = "/api/library/book";
    private final String LIBRARY_URL_GET_ALL_AUTHOR = "/api/library/author";
    private final String LIBRARY_URL_GET_ALL_BOOK_TYPE = "/api/library/book-type";

    @Autowired
    private BookESRepository bookESRepository;

    @Autowired
    private AuthorESRepository authorESRepository;

    @Autowired
    private BookTypeESRepository bookTypeESRepository;

    @Autowired
    private BookESConverter bookESConverter;

    @Autowired
    private RabbitMQClient client;


    @Override
    public List<BookES> synchronize(List<BookDTO> books, List<AuthorDTO> authors,
                                    List<BookTypeDTO> bookTypes) {
        try {
            List<BookES> bookESs = books.stream().map(e -> {
                return bookESConverter.convertModel2ES(e);
            }).collect(Collectors.toList());
            List<AuthorES> authorESs = authors.stream().map(e -> {
                return bookESConverter.convertAuthorModel2AuthorES(e);
            }).collect(Collectors.toList());
            List<BookTypeES> bookTypeESs = bookTypes.stream().map(e -> {
                return bookESConverter.convertBookTypeModel2BookTypeES(e);
            }).collect(Collectors.toList());
            bookESs = (List<BookES>) bookESRepository.saveAll(bookESs);
            authorESRepository.saveAll(authorESs);
            bookTypeESRepository.saveAll(bookTypeESs);
            return bookESs;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<BookES> findByName(String name) {
        try {
            return bookESRepository.findBookESByName(name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<BookDTO> getAllOnLibrary(Map<String, String> headerParam) {
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("GET");
        userRpcRequest.setRequestPath(LIBRARY_URL_GET_ALL_BOOK);
        userRpcRequest.setBodyParam(null);
        userRpcRequest.setUrlParam(null);
        userRpcRequest.setHeaderParam(headerParam);
        String result = client.callRpcService(RabbitMQProperties.LIBRARY_RPC_EXCHANGE,
                RabbitMQProperties.LIBRARY_RPC_QUEUE, RabbitMQProperties.LIBRARY_RPC_KEY, userRpcRequest.toJsonString());
        log.info("get all book in library - result: " + result);
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
                        List<BookDTO> dtos = null;
                        if (data.getClass() == ArrayList.class) {
                            List<LinkedHashMap> list = (List<LinkedHashMap>) data;
                            dtos = new ArrayList<>();
                            for(LinkedHashMap l:list) {
                                dtos.add(new BookDTO(l));
                            }
                        }
                        if (dtos != null) {
                            return dtos;
                        }
                    }
                } catch (Exception ex) {
                    log.info("Lỗi giải mã getAllOnLibrary khi gọi library service verify: " + ex.toString());
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

    public List<AuthorDTO> getAllAuthorOnLibrary(Map<String, String> headerParam) {
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("GET");
        userRpcRequest.setRequestPath(LIBRARY_URL_GET_ALL_AUTHOR);
        userRpcRequest.setBodyParam(null);
        userRpcRequest.setUrlParam(null);
        userRpcRequest.setHeaderParam(headerParam);
        String result = client.callRpcService(RabbitMQProperties.LIBRARY_RPC_EXCHANGE,
                RabbitMQProperties.LIBRARY_RPC_QUEUE, RabbitMQProperties.LIBRARY_RPC_KEY, userRpcRequest.toJsonString());
        log.info("get all author in library - result: " + result);
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
                        List<AuthorDTO> dtos = null;
                        if (data.getClass() == ArrayList.class) {
                            List<LinkedHashMap> list = (List<LinkedHashMap>) data;
                            dtos = new ArrayList<>();
                            for(LinkedHashMap l:list) {
                                dtos.add(new AuthorDTO(l));
                            }
                        }
                        if (dtos != null) {
                            return dtos;
                        }
                    }
                } catch (Exception ex) {
                    log.info("Lỗi giải mã getAllAuthorOnLibrary khi gọi library service verify: " + ex.toString());
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

    public List<BookTypeDTO> getAllBookTypeOnLibrary(Map<String, String> headerParam) {
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("GET");
        userRpcRequest.setRequestPath(LIBRARY_URL_GET_ALL_BOOK_TYPE);
        userRpcRequest.setBodyParam(null);
        userRpcRequest.setUrlParam(null);
        userRpcRequest.setHeaderParam(headerParam);
        String result = client.callRpcService(RabbitMQProperties.LIBRARY_RPC_EXCHANGE,
                RabbitMQProperties.LIBRARY_RPC_QUEUE, RabbitMQProperties.LIBRARY_RPC_KEY, userRpcRequest.toJsonString());
        log.info("get all book-type in library - result: " + result);
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
                        List<BookTypeDTO> dtos = null;
                        if (data.getClass() == ArrayList.class) {
                            List<LinkedHashMap> list = (List<LinkedHashMap>) data;
                            dtos = new ArrayList<>();
                            for(LinkedHashMap l:list) {
                                dtos.add(new BookTypeDTO(l));
                            }
                        }
                        if (dtos != null) {
                            return dtos;
                        }
                    }
                } catch (Exception ex) {
                    log.info("Lỗi giải mã getAllBookTypeOnLibrary khi gọi library service verify: " + ex.toString());
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
}
