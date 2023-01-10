package org.micro.dto;

import lombok.Data;

import java.util.Map;

@Data
public class BookDTO {
    private Integer id;
    private String name;
    private String content;
    private Integer author_id;
    private Integer type_id;

    public BookDTO(String name, String content, Integer author_id, Integer type_id) {
        this.name = name;
        this.content = content;
        this.author_id = author_id;
        this.type_id = type_id;
    }

    public BookDTO() {}

    public BookDTO(Integer id, String name, String content, Integer author_id, Integer type_id) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.author_id = author_id;
        this.type_id = type_id;
    }

    public BookDTO(Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            if (map.containsKey("id")) {
                this.id = (Integer) map.get("id");
            }
            if (map.containsKey("name")) {
                this.name = (String) map.get("name");
            }
            if (map.containsKey("content")) {
                this.content = (String) map.get("content");
            }
            if (map.containsKey("author_id")) {
                this.author_id = (Integer) map.get("author_id");
            }
            if (map.containsKey("type_id")) {
                this.type_id = (Integer) map.get("type_id");
            }
        }
    }
}
