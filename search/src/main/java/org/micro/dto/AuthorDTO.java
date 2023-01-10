package org.micro.dto;

import lombok.Data;

import java.util.Map;

@Data
public class AuthorDTO {
    private Integer id;
    private String name;

    public AuthorDTO(Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            if (map.containsKey("id")) {
                this.id = (Integer) map.get("id");
            }
            if (map.containsKey("name")) {
                this.name = (String) map.get("name");
            }
        }
    }
}
