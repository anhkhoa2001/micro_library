package org.micro.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


@Data
public class ResponseMessage {

    private int status;
    private String message;
    private MessageContent data;

    public ResponseMessage() {
    }
    
    public ResponseMessage(MessageContent data) {
        this.status = 200;
        this.message = "200 OK";
        this.data = data;
    }

    public ResponseMessage(int status, String message, MessageContent data) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    public String toJsonString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(df);
        return mapper.writeValueAsString(this);
    }
}
