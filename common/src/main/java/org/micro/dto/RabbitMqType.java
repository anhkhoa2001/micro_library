package org.micro.dto;

import lombok.Data;

import java.util.Set;

@Data
public class RabbitMqType {
    private String path;
    private String method;
    private String rabbit;
    private Set<String> role;
}
