package org.micro.dto;

import lombok.Data;

@Data
public class ResponseAuthentication {
    private String token;
    private String username;
    private String active;

    public ResponseAuthentication(String token, String username, String active) {
        this.token = token;
        this.username = username;
        this.active = active;
    }
}
