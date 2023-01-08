package org.micro.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

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
}
