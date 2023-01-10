package org.micro.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "booktypeindex")
public class BookTypeES {
    @Id
    private Integer id;
    @Field(type = FieldType.Text, name = "name")
    private String name;
}
