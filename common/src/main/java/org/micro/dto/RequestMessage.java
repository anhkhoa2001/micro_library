
package org.micro.dto;

import com.google.gson.Gson;
import lombok.Data;

import java.util.Map;

@Data
public class RequestMessage {

    private String requestMethod;
    private String requestPath;
    private Map<String, String> urlParam;
    private String pathParam;
    private Map<String, Object> bodyParam;
    private Map<String, String> headerParam;

    public RequestMessage() {
    }

    public RequestMessage(String requestMethod, String requestPath, Map<String, String> urlParam,
            String pathParam, Map<String, Object> bodyParam, Map<String, String> headerParam) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
        this.urlParam = urlParam;
        this.pathParam = pathParam;
        this.bodyParam = bodyParam;
        this.headerParam = headerParam;
    }

    public String toJsonString(){
        return new Gson().toJson(this);
    }
}
