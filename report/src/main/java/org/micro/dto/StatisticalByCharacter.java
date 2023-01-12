package org.micro.dto;

import lombok.Data;

import java.util.Map;

@Data
public class StatisticalByCharacter {

    private Character book_character;
    private Integer total;

    public StatisticalByCharacter(Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            if (map.containsKey("book_character")) {
                this.book_character = map.get("book_character").toString().charAt(0);
            }
            if (map.containsKey("total")) {
                this.total = (Integer) map.get("total");
            }
        }
    }

    public StatisticalByCharacter() {}
}
