package org.micro.dto;

import lombok.Data;
import org.micro.enums.StatusBorrowBook;

import java.util.Map;

@Data
public class UserDTO {

    private Integer id;
    private String username;
    private String password;

    public UserDTO(Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            if (map.containsKey("id")) {
                this.id = (Integer) map.get("id");
            }
            if (map.containsKey("username")) {
                this.username = (String) map.get("username");
            }
            if (map.containsKey("password")) {
                this.password = (String) map.get("password");
            }
        }
    }
}
