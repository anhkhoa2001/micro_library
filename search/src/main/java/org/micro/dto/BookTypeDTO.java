package org.micro.dto;

import lombok.Data;

import java.util.Map;

@Data
public class BookTypeDTO {

    private Integer type_id;
    private String name;

    public BookTypeDTO(Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            if (map.containsKey("type_id")) {
                this.type_id = (Integer) map.get("type_id");
            }
            if (map.containsKey("name")) {
                this.name = (String) map.get("name");
            }
        }
    }
}
