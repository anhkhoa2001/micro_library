package org.micro.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.micro.constant.ResourcePath;
import org.micro.controller.LoginController;
import org.micro.controller.UserController;
import org.micro.dto.RequestMessage;
import org.micro.dto.ResponseMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class RpcServer {

    @Autowired
    private LoginController loginController;

    @Autowired
    private UserController userController;

    @RabbitListener(queues = "${user.rpc.queue}")
    public String processService(String json) {
        try {
            log.info(" [-->] Server received request for " + json);
            ObjectMapper mapper = new ObjectMapper();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mapper.setDateFormat(df);
            RequestMessage request = mapper.readValue(json, RequestMessage.class);

            //Process here
            ResponseMessage response = new ResponseMessage(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
            if (request != null) {
                String requestPath = request.getRequestPath().replace(ResourcePath.API, "");
                Map<String, String> urlParam = request.getUrlParam();
                String pathParam = request.getPathParam();
                Map<String, Object> bodyParam = request.getBodyParam();
                Map<String, String> headerParam = request.getHeaderParam();
                Set<String> role = request.getRole();
                log.info(" [-->] Server received requestPath =========>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + requestPath);

                switch (request.getRequestMethod()) {
                    case "GET":
                        if(requestPath.contains("/user/get-by-id")) {
                            response = userController.getById(requestPath, urlParam, headerParam);
                        }
                        break;
                    case "POST":
                        if("/user/login".equalsIgnoreCase(requestPath)) {
                            response = loginController.login(requestPath, headerParam, bodyParam);
                        } else if("/user/register".equalsIgnoreCase(requestPath)) {
                            response = loginController.register(requestPath, headerParam, bodyParam);
                        } else if("/user/authority".equalsIgnoreCase(requestPath)) {
                            response = loginController.addAuthority(requestPath, headerParam, bodyParam, role);
                        } else if("/user/authorization".equalsIgnoreCase(requestPath)) {
                            response = loginController.authorization(requestPath, headerParam);
                        } else if("/user".equalsIgnoreCase(requestPath)) {
                            response = userController.getAllUser(requestPath, headerParam, bodyParam);
                        }
                        break;
                    case "PUT":

                    case "PATCH":
                        break;
                    case "DELETE":

                    default:
                        break;
                }
            }
            log.info(" [<--] Server returned " + response != null ? response.toJsonString() : "null");
            return response != null ? response.toJsonString() : null;
        } catch (Exception ex) {
            log.error("Error to processService >>> " + ex.getMessage());
            ex.printStackTrace();
        }
        return null;
    }
}
