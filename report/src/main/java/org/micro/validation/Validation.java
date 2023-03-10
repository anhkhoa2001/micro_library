package org.micro.validation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.micro.dto.AuthorizationDTO;
import org.micro.dto.MessageContent;
import org.micro.dto.RequestMessage;
import org.micro.dto.ResponseMessage;
import org.micro.mq.RabbitMQClient;
import org.micro.mq.RabbitMQProperties;
import org.micro.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class Validation {
    @Autowired
    private RabbitMQClient client;
    private final String USER_URL_AUTHENTICATION = "/api/user/authentication";

    public AuthorizationDTO validateHeader(Map<String, String> headerParam) {
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
                return null;
            }
        } else {
            return null;
        }
        return null;
    }
}
