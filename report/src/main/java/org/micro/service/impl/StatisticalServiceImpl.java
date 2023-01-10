package org.micro.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.micro.dto.*;
import org.micro.mq.RabbitMQClient;
import org.micro.mq.RabbitMQProperties;
import org.micro.service.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class StatisticalServiceImpl implements StatisticalService {
    @Autowired
    private RabbitMQClient client;

    private final String LIBRARY_URL_AUTHOR = "/library/statistical/author";
    private final String LIBRARY_URL_CHARACTER = "/library/statistical/character";
    private final String LIBRARY_URL_TYPE = "/library/statistical/type";
    private final String LIBRARY_URL_GET_BY_ID = "/library/book/get-by-id";
    private final String LOAN_BOOK_URL_MOST_COUNT = "/book_loan/get-count-by-time";
    private final String LOAN_BOOK_URL_MOST_TIME = "/book_loan/get-id-most-borrow";

    @Override
    public List<StatisticalByAuthor> getAllByAuthor(Map<String, String> headerParam) {
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("GET");
        userRpcRequest.setRequestPath(LIBRARY_URL_AUTHOR);
        userRpcRequest.setBodyParam(null);
        userRpcRequest.setUrlParam(null);
        userRpcRequest.setHeaderParam(headerParam);
        String result = client.callRpcService(RabbitMQProperties.LIBRARY_RPC_EXCHANGE,
                RabbitMQProperties.LIBRARY_RPC_QUEUE, RabbitMQProperties.LIBRARY_RPC_KEY, userRpcRequest.toJsonString());
        log.info("statistical author result: " + result);
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
                        List<StatisticalByAuthor> dtos = null;
                        if (data.getClass() == StatisticalByAuthor.class) {
                            dtos = (List<StatisticalByAuthor>) data;
                        } else if(data.getClass() == ArrayList.class) {
                            List<LinkedHashMap<String, Object>> rs = (List<LinkedHashMap<String, Object>>) data;
                            dtos = new ArrayList<>();
                            for(Object o:rs) {
                                if(o.getClass() == LinkedHashMap.class) {
                                    dtos.add(new StatisticalByAuthor((LinkedHashMap) o));
                                }
                            }
                        }
                        if (dtos != null) {
                            return dtos;
                        }
                    }
                } catch (Exception ex) {
                    log.info("Lỗi giải mã StatisticalByAuthor khi gọi user service verify: " + ex.toString());
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

    @Override
    public List<StatisticalByType> getAllByType(Map<String, String> headerParam) {
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("GET");
        userRpcRequest.setRequestPath(LIBRARY_URL_TYPE);
        userRpcRequest.setBodyParam(null);
        userRpcRequest.setUrlParam(null);
        userRpcRequest.setHeaderParam(headerParam);
        String result = client.callRpcService(RabbitMQProperties.LIBRARY_RPC_EXCHANGE,
                RabbitMQProperties.LIBRARY_RPC_QUEUE, RabbitMQProperties.LIBRARY_RPC_KEY, userRpcRequest.toJsonString());
        log.info("statistical type result: " + result);
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
                        List<StatisticalByType> dtos = null;
                        if (data.getClass() == StatisticalByType.class) {
                            dtos = (List<StatisticalByType>) data;
                        } else if(data.getClass() == ArrayList.class) {
                            List<LinkedHashMap<String, Object>> rs = (List<LinkedHashMap<String, Object>>) data;
                            dtos = new ArrayList<>();
                            for(Object o:rs) {
                                if(o.getClass() == LinkedHashMap.class) {
                                    dtos.add(new StatisticalByType((LinkedHashMap) o));
                                }
                            }
                        }
                        if (dtos != null) {
                            return dtos;
                        }
                    }
                } catch (Exception ex) {
                    log.info("Lỗi giải mã StatisticalByType khi gọi user service verify: " + ex.toString());
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

    @Override
    public List<StatisticalByCharacter> getAllByFirstCharacter(Map<String, String> headerParam) {
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("GET");
        userRpcRequest.setRequestPath(LIBRARY_URL_CHARACTER);
        userRpcRequest.setBodyParam(null);
        userRpcRequest.setUrlParam(null);
        userRpcRequest.setHeaderParam(headerParam);
        String result = client.callRpcService(RabbitMQProperties.LIBRARY_RPC_EXCHANGE,
                RabbitMQProperties.LIBRARY_RPC_QUEUE, RabbitMQProperties.LIBRARY_RPC_KEY, userRpcRequest.toJsonString());
        log.info("statistical author result: " + result);
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
                        List<StatisticalByCharacter> dtos = null;
                        if (data.getClass() == StatisticalByCharacter.class) {
                            dtos = (List<StatisticalByCharacter>) data;
                        } else if(data.getClass() == ArrayList.class) {
                            List<LinkedHashMap<String, Object>> rs = (List<LinkedHashMap<String, Object>>) data;
                            dtos = new ArrayList<>();
                            for(Object o:rs) {
                                if(o.getClass() == LinkedHashMap.class) {
                                    dtos.add(new StatisticalByCharacter((LinkedHashMap) o));
                                }
                            }
                        }
                        if (dtos != null) {
                            return dtos;
                        }
                    }
                } catch (Exception ex) {
                    log.info("Lỗi giải mã StatisticalByCharacter khi gọi user service verify: " + ex.toString());
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

    @Override
    public Long countByTimer(Map<String, String> headerParam, Map<String, Object> body) {
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("POST");
        userRpcRequest.setRequestPath(LOAN_BOOK_URL_MOST_COUNT);
        userRpcRequest.setBodyParam(body);
        userRpcRequest.setUrlParam(null);
        userRpcRequest.setHeaderParam(headerParam);
        String result = client.callRpcService(RabbitMQProperties.BOOK_LOAN_RPC_EXCHANGE,
                RabbitMQProperties.BOOK_LOAN_RPC_QUEUE, RabbitMQProperties.BOOK_LOAN_RPC_KEY, userRpcRequest.toJsonString());
        log.info("statistical count result: " + result);
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
                        return Long.parseLong(data.toString());
                    }
                } catch (Exception ex) {
                    log.info("Lỗi giải mã countByTimer khi gọi user service verify: " + ex.toString());
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

    @Override
    public Integer getBookIdByTimer(Map<String, String> headerParam, Map<String, Object> body) {
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("POST");
        userRpcRequest.setRequestPath(LOAN_BOOK_URL_MOST_TIME);
        userRpcRequest.setBodyParam(body);
        userRpcRequest.setUrlParam(null);
        userRpcRequest.setHeaderParam(headerParam);
        String result = client.callRpcService(RabbitMQProperties.BOOK_LOAN_RPC_EXCHANGE,
                RabbitMQProperties.BOOK_LOAN_RPC_QUEUE, RabbitMQProperties.BOOK_LOAN_RPC_KEY, userRpcRequest.toJsonString());
        log.info("statistical count result: " + result);
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
                        return (Integer) data;
                    }
                } catch (Exception ex) {
                    log.info("Lỗi giải mã getBookIdByTimer khi gọi user service verify: " + ex.toString());
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

    @Override
    public Object getBookById(Integer id, Map<String, String> headerParam) {
        RequestMessage userRpcRequest = new RequestMessage();
        userRpcRequest.setRequestMethod("GET");
        userRpcRequest.setRequestPath(LIBRARY_URL_GET_BY_ID);
        userRpcRequest.setBodyParam(null);
        userRpcRequest.setHeaderParam(headerParam);
        Map<String, String> urlParam = new HashMap<>();
        urlParam.put("id", id.toString());
        userRpcRequest.setUrlParam(urlParam);
        String result = client.callRpcService(RabbitMQProperties.LIBRARY_RPC_EXCHANGE,
                RabbitMQProperties.LIBRARY_RPC_QUEUE, RabbitMQProperties.LIBRARY_RPC_KEY, userRpcRequest.toJsonString());
        log.info("book by id result: " + result);
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
                        return data;
                    }
                } catch (Exception ex) {
                    log.info("Lỗi giải mã getBookById khi gọi user service verify: " + ex.toString());
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
