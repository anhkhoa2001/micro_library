package org.micro.dto;

import lombok.Data;

import java.util.Map;

@Data
public class StatisticalByType {

    private Integer book_type_id;
    private String book_type_name;
    private Integer total;

    public StatisticalByType(Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            if (map.containsKey("book_type_name")) {
                this.book_type_name = map.get("book_type_name").toString();
            }
            if (map.containsKey("book_type_id")) {
                this.book_type_id = (Integer) map.get("book_type_id");
            }
            if (map.containsKey("total")) {
                this.total = (Integer) map.get("total");
            }
        }
    }

    public StatisticalByType() {}
}
