package org.micro.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.micro.constant.ResourcePath;
import org.micro.controller.AuthorController;
import org.micro.controller.BookController;
import org.micro.controller.BookTypeController;
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
    private BookController bookController;

    @Autowired
    private AuthorController authorController;

    @Autowired
    private BookTypeController bookTypeController;

    @RabbitListener(queues = "${library.rpc.queue}")
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
                        if("/library/book".equalsIgnoreCase(requestPath)) {
                            response = bookController.getAll(requestPath, headerParam);
                        } else if("/library/author".equalsIgnoreCase(requestPath)) {
                            response = authorController.getAll(requestPath, headerParam);
                        } else if("/library/book-type".equalsIgnoreCase(requestPath)) {
                            response = bookTypeController.getAll(requestPath, headerParam);
                        }
                        break;
                    case "POST":
                        if("/library/book".equalsIgnoreCase(requestPath)) {
                            response = bookController.addBook(requestPath, headerParam, bodyParam);
                        } else if("/library/author".equalsIgnoreCase(requestPath)) {
                            response = authorController.addAuthor(requestPath, headerParam, bodyParam);
                        } else if("/library/book-type".equalsIgnoreCase(requestPath)) {
                            response = bookTypeController.addBookType(requestPath, headerParam, bodyParam);
                        }
                        break;
                    case "PUT":
                        if("/library/book".equalsIgnoreCase(requestPath)) {
                            response = bookController.edit(requestPath, headerParam, bodyParam);
                        } else if("/library/author".equalsIgnoreCase(requestPath)) {
                            response = authorController.edit(requestPath, headerParam, bodyParam);
                        } else if("/library/book-type".equalsIgnoreCase(requestPath)) {
                            response = bookTypeController.edit(requestPath, headerParam, bodyParam);
                        }
                        break;
                    case "PATCH":
                        break;
                    case "DELETE":
                        if("/library/book".equalsIgnoreCase(requestPath)) {
                            response = bookController.delete(requestPath, headerParam, urlParam);
                        } else if("/library/author".equalsIgnoreCase(requestPath)) {
                            response = authorController.delete(requestPath, headerParam, urlParam);
                        } else if("/library/book-type".equalsIgnoreCase(requestPath)) {
                            response = bookTypeController.delete(requestPath, headerParam, urlParam);
                        }
                        break;
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
