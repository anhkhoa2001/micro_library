package org.micro.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.micro.constant.ResourcePath;
import org.micro.controller.BorrowBookController;
import org.micro.controller.StatisticalBorrowController;
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
    private BorrowBookController borrowBookController;

    @Autowired
    private StatisticalBorrowController statisticalBorrowController;

    @RabbitListener(queues = "${book_loan.rpc.queue}")
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
                log.info(" [-->] Server received requestPath =========>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + requestPath);

                switch (request.getRequestMethod()) {
                    case "GET":
                        if("/book_loan/borrow_book".equalsIgnoreCase(requestPath)) {
                            response = borrowBookController.getAll(requestPath, headerParam);
                        }
                        break;
                    case "POST":
                        if("/book_loan/borrow_book".equalsIgnoreCase(requestPath)) {
                            response = borrowBookController.borrowBook(requestPath, headerParam, bodyParam);
                        } else if("/book_loan/return_book".equalsIgnoreCase(requestPath)) {
                            response = borrowBookController.returnBook(requestPath, headerParam, bodyParam);
                        } else if("/book_loan/get-count-by-time".equalsIgnoreCase(requestPath)) {
                            response = statisticalBorrowController.getCountByTime(requestPath, headerParam, bodyParam);
                        } else if("/book_loan/get-id-most-borrow".equalsIgnoreCase(requestPath)) {
                            response = statisticalBorrowController.getBookIdByTime(requestPath, headerParam, bodyParam);
                        }
                        break;
                    case "PUT":
                        break;
                    case "PATCH":
                        break;
                    case "DELETE":
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
