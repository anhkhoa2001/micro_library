package org.micro.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class BookDTO {
    private Integer id;
    private String name;
    private String content;
    private AuthorDTO author;
    private BookTypeDTO book_type;

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
            if (map.containsKey("author")) {
                if(map.get("author").getClass() == LinkedHashMap.class) {
                    this.author = new AuthorDTO((LinkedHashMap) map.get("author"));
                }
            }
            if (map.containsKey("bookType")) {
                if(map.get("bookType").getClass() == LinkedHashMap.class) {
                    this.book_type = new BookTypeDTO((LinkedHashMap) map.get("bookType"));
                }
            }
        }
    }
}
