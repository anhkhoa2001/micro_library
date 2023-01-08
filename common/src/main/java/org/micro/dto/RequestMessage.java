/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.micro.dto;

import com.google.gson.Gson;
import lombok.Data;

import java.util.Map;
import java.util.Set;


@Data
public class RequestMessage {

    private String requestMethod;
    private String requestPath;
    private Map<String, String> urlParam;
    private String pathParam;
    private Map<String, Object> bodyParam;
    private Map<String, String> headerParam;
    private Set<String> role;

    public RequestMessage() {
    }

    public RequestMessage(String requestMethod, String requestPath, Map<String, String> urlParam,
            String pathParam, Map<String, Object> bodyParam, Map<String, String> headerParam,
                          Set<String> role) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
        this.urlParam = urlParam;
        this.pathParam = pathParam;
        this.bodyParam = bodyParam;
        this.headerParam = headerParam;
        this.role = role;
    }

    public String toJsonString(){
        return new Gson().toJson(this);
    }
}
