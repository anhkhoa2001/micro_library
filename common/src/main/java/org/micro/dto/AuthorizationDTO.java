package org.micro.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class AuthorizationDTO {
    private String id;
    private String username;
    private String password;
    private Boolean status;
    private Set<Object> authorities;

    public AuthorizationDTO(Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            if (map.containsKey("id")) {
                this.id = ((Integer) map.get("id")).toString();
            }
            if (map.containsKey("username")) {
                this.username = (String) map.get("username");
            }
            if (map.containsKey("password")) {
                this.password = (String) map.get("password");
            }
            if (map.containsKey("status")) {
                this.status = (Boolean) map.get("status");
            }
            if (map.containsKey("authorities")) {
                this.authorities = new HashSet<>((List<Object>) map.get("authorities"));
            }
        }
    }
}
