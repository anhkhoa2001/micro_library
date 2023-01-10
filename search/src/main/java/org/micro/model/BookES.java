package org.micro.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "bookindex")
@Data
public class BookES {
    @Id
    private Integer id;
    @Field(type = FieldType.Text, name = "name")
    private String name;
    @Field(type = FieldType.Text, name = "content")
    private String content;
    @Field(type = FieldType.Integer, name = "author_id")
    private Integer author_id;
    @Field(type = FieldType.Integer, name = "type_id")
    private Integer type_id;
}
