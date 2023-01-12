package org.micro.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Data
public class StatisticalByAuthor {
    private String author_name;
    private Integer author_id;
    private Integer total;

    public StatisticalByAuthor(Map<String, Object> map) {
        if (map != null && map.size() > 0) {
            if (map.containsKey("author_name")) {
                this.author_name = map.get("author_name").toString();
            }
            if (map.containsKey("author_id")) {
                this.author_id = (Integer) map.get("author_id");
            }
            if (map.containsKey("total")) {
                this.total = (Integer) map.get("total");
            }
        }
    }

    public StatisticalByAuthor() {}
}
